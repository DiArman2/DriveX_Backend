package com.espe.drivex.service;

import com.espe.drivex.entity.EstadoPago;
import com.espe.drivex.exception.ResourceNotFoundException;
import com.espe.drivex.repository.EstadoPagoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EstadoPagoService {

    @Autowired
    private EstadoPagoRepository estadoPagoRepository;

    public List<EstadoPago> listarTodos() {
        return estadoPagoRepository.findAll().stream()
                .filter(EstadoPago::getActivo)
                .collect(Collectors.toList());
    }

    public EstadoPago buscarPorId(Long id) {
        return estadoPagoRepository.findById(id)
                .filter(EstadoPago::getActivo)
                .orElseThrow(() -> new ResourceNotFoundException("EstadoPago no encontrado con id: " + id));
    }

    public EstadoPago guardar(EstadoPago entidad) {
        validar(entidad);
        entidad.setActivo(true);
        return estadoPagoRepository.save(entidad);
    }

    public EstadoPago actualizar(Long id, EstadoPago datos) {
        EstadoPago existente = buscarPorId(id);
        validar(datos);
        existente.setNombre(datos.getNombre());
        return estadoPagoRepository.save(existente);
    }

    public void eliminar(Long id) {
        EstadoPago existente = buscarPorId(id);
        existente.setActivo(false);
        estadoPagoRepository.save(existente);
    }

    private void validar(EstadoPago entidad) {
        if (entidad.getNombre() == null || entidad.getNombre().isBlank()) {
            throw new RuntimeException("El nombre es obligatorio");
        }
    }
}
