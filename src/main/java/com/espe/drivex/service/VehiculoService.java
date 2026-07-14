package com.espe.drivex.service;

import com.espe.drivex.dto.VehiculoRequest;
import com.espe.drivex.dto.VehiculoResponse;
import com.espe.drivex.entity.Usuario;
import com.espe.drivex.entity.Vehiculo;
import com.espe.drivex.entity.Version;
import com.espe.drivex.exception.ConflictException;
import com.espe.drivex.exception.ResourceNotFoundException;
import com.espe.drivex.repository.UsuarioRepository;
import com.espe.drivex.repository.VehiculoRepository;
import com.espe.drivex.repository.VersionRepository;
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
public class VehiculoService {

    private static final Logger log = LoggerFactory.getLogger(VehiculoService.class);

    private final VehiculoRepository vehiculoRepository;
    private final VersionRepository versionRepository;
    private final UsuarioRepository usuarioRepository;

    public VehiculoService(VehiculoRepository vehiculoRepository,
                           VersionRepository versionRepository,
                           UsuarioRepository usuarioRepository) {
        this.vehiculoRepository = vehiculoRepository;
        this.versionRepository = versionRepository;
        this.usuarioRepository = usuarioRepository;
    }

    // ── READ ──────────────────────────────────────────────────────────────────

    @Transactional(readOnly = true)
    public List<VehiculoResponse> listarTodos() {
        log.info("Listando todos los vehículos activos");
        return vehiculoRepository.findAllByActivoTrue()
                .stream().map(this::toResponse).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public VehiculoResponse buscarPorId(Long id) {
        log.info("Buscando vehículo con id: {}", id);
        return toResponse(findActivo(id));
    }

    @Transactional(readOnly = true)
    public List<VehiculoResponse> listarDisponibles() {
        log.info("Listando vehículos disponibles");
        return vehiculoRepository.findAllByActivoTrueAndDisponibleTrue()
                .stream().map(this::toResponse).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<VehiculoResponse> buscarPorColor(String color) {
        log.info("Buscando vehículos por color: {}", color);
        return vehiculoRepository.findByColorContainingIgnoreCaseAndActivoTrue(color)
                .stream().map(this::toResponse).collect(Collectors.toList());
    }

    // ── CREATE ────────────────────────────────────────────────────────────────

    public VehiculoResponse guardar(VehiculoRequest request) {
        log.info("Registrando vehículo con placa: {}", request.getPlaca());

        if (vehiculoRepository.findByPlaca(request.getPlaca()).isPresent()) {
            throw new ConflictException("Ya existe un vehículo registrado con la placa: " + request.getPlaca());
        }

        Version version = versionRepository.findById(request.getVersionId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Versión no encontrada con id: " + request.getVersionId()));

        Usuario propietario = usuarioRepository.findById(request.getPropietarioId())
                .filter(Usuario::getActivo)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Propietario no encontrado con id: " + request.getPropietarioId()));

        validarCampos(request);

        Vehiculo vehiculo = new Vehiculo();
        vehiculo.setVersion(version);
        vehiculo.setPropietario(propietario);
        vehiculo.setPlaca(request.getPlaca());
        vehiculo.setColor(request.getColor());
        vehiculo.setTransmision(request.getTransmision());
        vehiculo.setTipoCombustible(request.getTipoCombustible());
        vehiculo.setAsientos(request.getAsientos());
        vehiculo.setPrecioDia(request.getPrecioDia());
        vehiculo.setDescripcion(request.getDescripcion());
        vehiculo.setUbicacion(request.getUbicacion());
        vehiculo.setDisponible(request.getDisponible() != null ? request.getDisponible() : true);
        vehiculo.setActivo(true);
        vehiculo.setFechaCreacion(LocalDateTime.now());
        vehiculo.setFechaActualizacion(LocalDateTime.now());

        Vehiculo guardado = vehiculoRepository.save(vehiculo);
        log.info("Vehículo creado con id: {}", guardado.getId());
        return toResponse(guardado);
    }

    // ── UPDATE ────────────────────────────────────────────────────────────────

    public VehiculoResponse actualizar(Long id, VehiculoRequest request) {
        log.info("Actualizando vehículo con id: {}", id);
        Vehiculo vehiculo = findActivo(id);

        vehiculoRepository.findByPlaca(request.getPlaca()).ifPresent(v -> {
            if (!v.getId().equals(id)) {
                throw new ConflictException("Ya existe un vehículo con la placa: " + request.getPlaca());
            }
        });

        Version version = versionRepository.findById(request.getVersionId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Versión no encontrada con id: " + request.getVersionId()));

        validarCampos(request);

        vehiculo.setVersion(version);
        vehiculo.setPlaca(request.getPlaca());
        vehiculo.setColor(request.getColor());
        vehiculo.setTransmision(request.getTransmision());
        vehiculo.setTipoCombustible(request.getTipoCombustible());
        vehiculo.setAsientos(request.getAsientos());
        vehiculo.setPrecioDia(request.getPrecioDia());
        vehiculo.setDescripcion(request.getDescripcion());
        vehiculo.setUbicacion(request.getUbicacion());
        vehiculo.setDisponible(request.getDisponible());
        vehiculo.setFechaActualizacion(LocalDateTime.now());

        return toResponse(vehiculoRepository.save(vehiculo));
    }

    // ── DELETE (lógico) ───────────────────────────────────────────────────────

    public void eliminar(Long id) {
        log.info("Desactivando vehículo con id: {}", id);
        Vehiculo vehiculo = findActivo(id);
        vehiculo.setActivo(false);
        vehiculo.setFechaActualizacion(LocalDateTime.now());
        vehiculoRepository.save(vehiculo);
    }

    // ── Helpers ───────────────────────────────────────────────────────────────

    private Vehiculo findActivo(Long id) {
        return vehiculoRepository.findById(id)
                .filter(Vehiculo::getActivo)
                .orElseThrow(() -> new ResourceNotFoundException("Vehículo no encontrado con id: " + id));
    }

    private void validarCampos(VehiculoRequest r) {
        List<String> transmisionesValidas = List.of("manual", "automatica");
        if (!transmisionesValidas.contains(r.getTransmision())) {
            throw new IllegalArgumentException("La transmisión debe ser: manual o automatica");
        }
        List<String> combustiblesValidos = List.of("gasolina", "diesel", "hibrido", "electrico");
        if (!combustiblesValidos.contains(r.getTipoCombustible())) {
            throw new IllegalArgumentException("El combustible debe ser: gasolina, diesel, hibrido o electrico");
        }
        if (r.getPrecioDia().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("El precio por día debe ser mayor a 0");
        }
    }

    private VehiculoResponse toResponse(Vehiculo v) {
        VehiculoResponse res = new VehiculoResponse();
        res.setId(v.getId());
        res.setVersionId(v.getVersion().getId());
        res.setVersionNombre(v.getVersion().getNombre());
        res.setVersionAnio(v.getVersion().getAnio());
        res.setModeloId(v.getVersion().getModelo().getId());
        res.setModeloNombre(v.getVersion().getModelo().getNombre());
        res.setMarcaId(v.getVersion().getModelo().getMarca().getId());
        res.setMarcaNombre(v.getVersion().getModelo().getMarca().getNombre());
        res.setPropietarioId(v.getPropietario().getId());
        res.setPropietarioNombres(v.getPropietario().getNombres());
        res.setPropietarioApellidos(v.getPropietario().getApellidos());
        res.setPlaca(v.getPlaca());
        res.setColor(v.getColor());
        res.setTransmision(v.getTransmision());
        res.setTipoCombustible(v.getTipoCombustible());
        res.setAsientos(v.getAsientos());
        res.setPrecioDia(v.getPrecioDia());
        res.setDescripcion(v.getDescripcion());
        res.setUbicacion(v.getUbicacion());
        res.setDisponible(v.getDisponible());
        res.setActivo(v.getActivo());
        res.setFechaCreacion(v.getFechaCreacion());
        res.setFechaActualizacion(v.getFechaActualizacion());
        return res;
    }
}
