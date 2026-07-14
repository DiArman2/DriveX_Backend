package com.espe.drivex.service;

import com.espe.drivex.dto.MarcaRequest;
import com.espe.drivex.dto.MarcaResponse;
import com.espe.drivex.entity.Marca;
import com.espe.drivex.exception.ConflictException;
import com.espe.drivex.exception.ResourceNotFoundException;
import com.espe.drivex.repository.MarcaRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class MarcaService {

    private static final Logger log = LoggerFactory.getLogger(MarcaService.class);

    private final MarcaRepository marcaRepository;

    public MarcaService(MarcaRepository marcaRepository) {
        this.marcaRepository = marcaRepository;
    }

    @Transactional(readOnly = true)
    public List<MarcaResponse> listarTodos() {
        log.info("Listando marcas activas");
        return marcaRepository.findAllByActivoTrue()
                .stream().map(this::toResponse).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public MarcaResponse buscarPorId(Long id) {
        return toResponse(findActivo(id));
    }

    @Transactional(readOnly = true)
    public List<MarcaResponse> buscarPorNombre(String nombre) {
        return marcaRepository.findByNombreContainingIgnoreCaseAndActivoTrue(nombre)
                .stream().map(this::toResponse).collect(Collectors.toList());
    }

    public MarcaResponse guardar(MarcaRequest request) {
        log.info("Registrando marca: {}", request.getNombre());
        marcaRepository.findByNombre(request.getNombre()).ifPresent(m -> {
            throw new ConflictException("Ya existe una marca con el nombre: " + request.getNombre());
        });
        Marca marca = new Marca();
        marca.setNombre(request.getNombre());
        marca.setActivo(true);
        marca.setFechaCreacion(LocalDateTime.now());
        return toResponse(marcaRepository.save(marca));
    }

    public MarcaResponse actualizar(Long id, MarcaRequest request) {
        log.info("Actualizando marca id: {}", id);
        Marca marca = findActivo(id);
        marcaRepository.findByNombre(request.getNombre()).ifPresent(m -> {
            if (!m.getId().equals(id)) {
                throw new ConflictException("Ya existe una marca con el nombre: " + request.getNombre());
            }
        });
        marca.setNombre(request.getNombre());
        return toResponse(marcaRepository.save(marca));
    }

    public void eliminar(Long id) {
        log.info("Desactivando marca id: {}", id);
        Marca marca = findActivo(id);
        marca.setActivo(false);
        marcaRepository.save(marca);
    }

    private Marca findActivo(Long id) {
        return marcaRepository.findById(id)
                .filter(Marca::getActivo)
                .orElseThrow(() -> new ResourceNotFoundException("Marca no encontrada con id: " + id));
    }

    private MarcaResponse toResponse(Marca m) {
        return new MarcaResponse(m.getId(), m.getNombre(), m.getActivo(), m.getFechaCreacion());
    }
}
