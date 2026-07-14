package com.espe.drivex.service;

import com.espe.drivex.dto.PagoRequest;
import com.espe.drivex.dto.PagoResponse;
import com.espe.drivex.entity.EstadoPago;
import com.espe.drivex.entity.MetodoPago;
import com.espe.drivex.entity.Pago;
import com.espe.drivex.entity.Reserva;
import com.espe.drivex.exception.ConflictException;
import com.espe.drivex.exception.ResourceNotFoundException;
import com.espe.drivex.repository.EstadoPagoRepository;
import com.espe.drivex.repository.MetodoPagoRepository;
import com.espe.drivex.repository.PagoRepository;
import com.espe.drivex.repository.ReservaRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class PagoService {

    private static final Logger log = LoggerFactory.getLogger(PagoService.class);

    private final PagoRepository pagoRepository;
    private final ReservaRepository reservaRepository;
    private final MetodoPagoRepository metodoPagoRepository;
    private final EstadoPagoRepository estadoPagoRepository;

    public PagoService(PagoRepository pagoRepository,
                       ReservaRepository reservaRepository,
                       MetodoPagoRepository metodoPagoRepository,
                       EstadoPagoRepository estadoPagoRepository) {
        this.pagoRepository = pagoRepository;
        this.reservaRepository = reservaRepository;
        this.metodoPagoRepository = metodoPagoRepository;
        this.estadoPagoRepository = estadoPagoRepository;
    }

    // ── READ ──────────────────────────────────────────────────────────────────

    @Transactional(readOnly = true)
    public List<PagoResponse> listarTodos() {
        log.info("Listando todos los pagos activos");
        return pagoRepository.findAll().stream()
                .filter(Pago::getActivo)
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public PagoResponse buscarPorId(Long id) {
        log.info("Buscando pago con id: {}", id);
        return toResponse(findActivo(id));
    }

    @Transactional(readOnly = true)
    public List<PagoResponse> buscarPorReserva(Long reservaId) {
        log.info("Buscando pagos de reserva id: {}", reservaId);
        return pagoRepository.findAll().stream()
                .filter(p -> p.getActivo() && p.getReserva().getId().equals(reservaId))
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    // ── CREATE — proceso transaccional ────────────────────────────────────────
    /**
     * Registra un pago vinculado a una reserva existente.
     * Si cualquier paso falla, @Transactional revierte todos los cambios.
     */
    public PagoResponse guardar(PagoRequest request) {
        log.info("Registrando pago para reserva id: {}", request.getReservaId());

        // 1. Validar reserva
        Reserva reserva = reservaRepository.findById(request.getReservaId())
                .filter(Reserva::getActivo)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Reserva no encontrada con id: " + request.getReservaId()));

        // 2. Validar método de pago
        MetodoPago metodoPago = metodoPagoRepository.findById(request.getMetodoPagoId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Método de pago no encontrado con id: " + request.getMetodoPagoId()));

        // 3. Validar estado de pago
        EstadoPago estadoPago = estadoPagoRepository.findById(request.getEstadoPagoId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Estado de pago no encontrado con id: " + request.getEstadoPagoId()));

        // 4. Validar monto
        if (request.getMonto() == null || request.getMonto().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("El monto debe ser mayor a cero");
        }

        // 5. Validar que el monto no supere el total de la reserva
        if (request.getMonto().compareTo(reserva.getTotalPagar()) > 0) {
            throw new ConflictException("El monto del pago (" + request.getMonto() +
                    ") no puede superar el total de la reserva (" + reserva.getTotalPagar() + ")");
        }

        // 6. Crear el pago
        Pago pago = new Pago();
        pago.setReserva(reserva);
        pago.setMetodoPago(metodoPago);
        pago.setEstadoPago(estadoPago);
        pago.setMonto(request.getMonto());
        pago.setReferencia(request.getReferencia());
        pago.setActivo(true);
        pago.setFechaPago(request.getFechaPago() != null ? request.getFechaPago() : LocalDateTime.now());

        Pago guardado = pagoRepository.save(pago);
        log.info("Pago registrado con id: {}", guardado.getId());
        return toResponse(guardado);
    }

    // ── UPDATE ────────────────────────────────────────────────────────────────

    public PagoResponse actualizar(Long id, PagoRequest request) {
        log.info("Actualizando pago con id: {}", id);
        Pago pago = findActivo(id);

        MetodoPago metodoPago = metodoPagoRepository.findById(request.getMetodoPagoId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Método de pago no encontrado con id: " + request.getMetodoPagoId()));

        EstadoPago estadoPago = estadoPagoRepository.findById(request.getEstadoPagoId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Estado de pago no encontrado con id: " + request.getEstadoPagoId()));

        if (request.getMonto() == null || request.getMonto().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("El monto debe ser mayor a cero");
        }

        pago.setMetodoPago(metodoPago);
        pago.setEstadoPago(estadoPago);
        pago.setMonto(request.getMonto());
        pago.setReferencia(request.getReferencia());

        return toResponse(pagoRepository.save(pago));
    }

    // ── DELETE (lógico) ───────────────────────────────────────────────────────

    public void eliminar(Long id) {
        log.info("Desactivando pago con id: {}", id);
        Pago pago = findActivo(id);
        pago.setActivo(false);
        pagoRepository.save(pago);
    }

    // ── Helpers ───────────────────────────────────────────────────────────────

    private Pago findActivo(Long id) {
        return pagoRepository.findById(id)
                .filter(Pago::getActivo)
                .orElseThrow(() -> new ResourceNotFoundException("Pago no encontrado con id: " + id));
    }

    private PagoResponse toResponse(Pago p) {
        PagoResponse res = new PagoResponse();
        res.setId(p.getId());
        res.setReservaId(p.getReserva().getId());
        res.setMetodoPagoId(p.getMetodoPago().getId());
        res.setMetodoPagoNombre(p.getMetodoPago().getNombre());
        res.setEstadoPagoId(p.getEstadoPago().getId());
        res.setEstadoPagoNombre(p.getEstadoPago().getNombre());
        res.setMonto(p.getMonto());
        res.setReferencia(p.getReferencia());
        res.setActivo(p.getActivo());
        res.setFechaPago(p.getFechaPago());
        return res;
    }
}
