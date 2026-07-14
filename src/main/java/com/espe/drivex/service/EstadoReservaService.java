package com.espe.drivex.service;

import com.espe.drivex.entity.EstadoReserva;
import com.espe.drivex.exception.ConflictException;
import com.espe.drivex.exception.ResourceNotFoundException;
import com.espe.drivex.repository.EstadoReservaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EstadoReservaService {

    @Autowired
    private EstadoReservaRepository estadoReservaRepository;

    public List<EstadoReserva> listarTodos() {
        return estadoReservaRepository.findAll().stream()
                .filter(EstadoReserva::getActivo)
                .collect(Collectors.toList());
    }

    public EstadoReserva buscarPorId(Long id) {
        return estadoReservaRepository.findById(id)
                .filter(EstadoReserva::getActivo)
                .orElseThrow(() -> new ResourceNotFoundException("EstadoReserva no encontrado con id: " + id));
    }

    public EstadoReserva guardar(EstadoReserva entidad) {
        validar(entidad);
        estadoReservaRepository.findByNombre(entidad.getNombre()).ifPresent(e -> {
            throw new ConflictException("Ya existe un estado de reserva con el nombre: " + entidad.getNombre());
        });
        entidad.setActivo(true);
        return estadoReservaRepository.save(entidad);
    }

    public EstadoReserva actualizar(Long id, EstadoReserva datos) {
        EstadoReserva existente = buscarPorId(id);
        validar(datos);
        existente.setNombre(datos.getNombre());
        return estadoReservaRepository.save(existente);
    }

    public void eliminar(Long id) {
        EstadoReserva existente = buscarPorId(id);
        existente.setActivo(false);
        estadoReservaRepository.save(existente);
    }

    private void validar(EstadoReserva entidad) {
        if (entidad.getNombre() == null || entidad.getNombre().isBlank()) {
            throw new RuntimeException("El nombre es obligatorio");
        }
    }
}
