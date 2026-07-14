package com.espe.drivex.service;

import com.espe.drivex.dto.ReservaRequest;
import com.espe.drivex.dto.ReservaResponse;
import com.espe.drivex.entity.EstadoReserva;
import com.espe.drivex.entity.Reserva;
import com.espe.drivex.entity.Usuario;
import com.espe.drivex.entity.Vehiculo;
import com.espe.drivex.exception.ConflictException;
import com.espe.drivex.exception.ResourceNotFoundException;
import com.espe.drivex.repository.EstadoReservaRepository;
import com.espe.drivex.repository.ReservaRepository;
import com.espe.drivex.repository.UsuarioRepository;
import com.espe.drivex.repository.VehiculoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class ReservaService {

    private static final Logger log = LoggerFactory.getLogger(ReservaService.class);

    private final ReservaRepository reservaRepository;
    private final UsuarioRepository usuarioRepository;
    private final VehiculoRepository vehiculoRepository;
    private final EstadoReservaRepository estadoReservaRepository;

    public ReservaService(ReservaRepository reservaRepository,
                          UsuarioRepository usuarioRepository,
                          VehiculoRepository vehiculoRepository,
                          EstadoReservaRepository estadoReservaRepository) {
        this.reservaRepository = reservaRepository;
        this.usuarioRepository = usuarioRepository;
        this.vehiculoRepository = vehiculoRepository;
        this.estadoReservaRepository = estadoReservaRepository;
    }

    // ── READ ──────────────────────────────────────────────────────────────────

    @Transactional(readOnly = true)
    public List<ReservaResponse> listarTodos() {
        log.info("Listando todas las reservas activas");
        return reservaRepository.findAllByActivoTrue()
                .stream().map(this::toResponse).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ReservaResponse buscarPorId(Long id) {
        log.info("Buscando reserva con id: {}", id);
        return toResponse(findActivo(id));
    }

    @Transactional(readOnly = true)
    public List<ReservaResponse> buscarPorCliente(Long clienteId) {
        log.info("Buscando reservas del cliente id: {}", clienteId);
        return reservaRepository.findByClienteIdAndActivoTrue(clienteId)
                .stream().map(this::toResponse).collect(Collectors.toList());
    }

    // ── CREATE — proceso transaccional multitabla ──────────────────────────────
    /**
     * Registra una reserva y actualiza la disponibilidad del vehículo.
     * Si cualquier paso falla, @Transactional revierte todos los cambios.
     */
    public ReservaResponse guardar(ReservaRequest request) {
        log.info("Iniciando registro de reserva para cliente id: {} y vehículo id: {}",
                request.getClienteId(), request.getVehiculoId());

        // 1. Validar cliente
        Usuario cliente = usuarioRepository.findById(request.getClienteId())
                .filter(Usuario::getActivo)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Cliente no encontrado con id: " + request.getClienteId()));

        // 2. Validar vehículo y disponibilidad
        Vehiculo vehiculo = vehiculoRepository.findById(request.getVehiculoId())
                .filter(Vehiculo::getActivo)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Vehículo no encontrado con id: " + request.getVehiculoId()));

        if (!vehiculo.getDisponible()) {
            throw new ConflictException("El vehículo con placa " + vehiculo.getPlaca() + " no está disponible");
        }

        // 3. Validar estado de reserva
        EstadoReserva estado = estadoReservaRepository.findById(request.getEstadoId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Estado de reserva no encontrado con id: " + request.getEstadoId()));

        // 4. Validar fechas
        if (!request.getFechaFin().isAfter(request.getFechaInicio())) {
            throw new IllegalArgumentException("La fecha de fin debe ser posterior a la fecha de inicio");
        }

        // 5. Calcular totales
        long dias = ChronoUnit.DAYS.between(request.getFechaInicio(), request.getFechaFin());
        BigDecimal totalPagar = vehiculo.getPrecioDia().multiply(BigDecimal.valueOf(dias));

        // 6. Crear la reserva
        Reserva reserva = new Reserva();
        reserva.setCliente(cliente);
        reserva.setVehiculo(vehiculo);
        reserva.setEstado(estado);
        reserva.setFechaInicio(request.getFechaInicio());
        reserva.setFechaFin(request.getFechaFin());
        reserva.setTotalDias((int) dias);
        reserva.setTotalPagar(totalPagar);
        reserva.setActivo(true);
        reserva.setFechaCreacion(LocalDateTime.now());
        reserva.setFechaActualizacion(LocalDateTime.now());
        Reserva guardada = reservaRepository.save(reserva);
        log.info("Reserva creada con id: {}", guardada.getId());

        // 7. Actualizar disponibilidad del vehículo
        vehiculo.setDisponible(false);
        vehiculo.setFechaActualizacion(LocalDateTime.now());
        vehiculoRepository.save(vehiculo);
        log.info("Vehículo id: {} marcado como no disponible", vehiculo.getId());

        return toResponse(guardada);
    }

    // ── UPDATE ────────────────────────────────────────────────────────────────

    public ReservaResponse actualizar(Long id, ReservaRequest request) {
        log.info("Actualizando reserva con id: {}", id);
        Reserva reserva = findActivo(id);

        EstadoReserva estado = estadoReservaRepository.findById(request.getEstadoId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Estado de reserva no encontrado con id: " + request.getEstadoId()));

        if (!request.getFechaFin().isAfter(request.getFechaInicio())) {
            throw new IllegalArgumentException("La fecha de fin debe ser posterior a la fecha de inicio");
        }

        reserva.setEstado(estado);
        reserva.setFechaInicio(request.getFechaInicio());
        reserva.setFechaFin(request.getFechaFin());

        long dias = ChronoUnit.DAYS.between(request.getFechaInicio(), request.getFechaFin());
        reserva.setTotalDias((int) dias);
        reserva.setTotalPagar(reserva.getVehiculo().getPrecioDia().multiply(BigDecimal.valueOf(dias)));
        reserva.setFechaActualizacion(LocalDateTime.now());

        return toResponse(reservaRepository.save(reserva));
    }

    // ── DELETE (lógico) + libera el vehículo ─────────────────────────────────

    public void eliminar(Long id) {
        log.info("Cancelando reserva con id: {}", id);
        Reserva reserva = findActivo(id);
        reserva.setActivo(false);
        reserva.setFechaActualizacion(LocalDateTime.now());
        reservaRepository.save(reserva);

        // Liberar el vehículo al cancelar la reserva
        Vehiculo vehiculo = reserva.getVehiculo();
        vehiculo.setDisponible(true);
        vehiculo.setFechaActualizacion(LocalDateTime.now());
        vehiculoRepository.save(vehiculo);
        log.info("Vehículo id: {} liberado al cancelar reserva id: {}", vehiculo.getId(), id);
    }

    // ── Helpers ───────────────────────────────────────────────────────────────

    private Reserva findActivo(Long id) {
        return reservaRepository.findById(id)
                .filter(Reserva::getActivo)
                .orElseThrow(() -> new ResourceNotFoundException("Reserva no encontrada con id: " + id));
    }

    private ReservaResponse toResponse(Reserva r) {
        ReservaResponse res = new ReservaResponse();
        res.setId(r.getId());
        res.setClienteId(r.getCliente().getId());
        res.setClienteNombres(r.getCliente().getNombres());
        res.setClienteApellidos(r.getCliente().getApellidos());
        res.setVehiculoId(r.getVehiculo().getId());
        res.setVehiculoPlaca(r.getVehiculo().getPlaca());
        res.setVehiculoMarca(r.getVehiculo().getVersion().getModelo().getMarca().getNombre());
        res.setVehiculoModelo(r.getVehiculo().getVersion().getModelo().getNombre());
        res.setEstadoId(r.getEstado().getId());
        res.setEstadoNombre(r.getEstado().getNombre());
        res.setFechaInicio(r.getFechaInicio());
        res.setFechaFin(r.getFechaFin());
        res.setTotalDias(r.getTotalDias());
        res.setTotalPagar(r.getTotalPagar());
        res.setActivo(r.getActivo());
        res.setFechaCreacion(r.getFechaCreacion());
        res.setFechaActualizacion(r.getFechaActualizacion());
        return res;
    }
}
