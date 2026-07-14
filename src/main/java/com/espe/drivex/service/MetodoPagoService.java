package com.espe.drivex.service;

import com.espe.drivex.entity.MetodoPago;
import com.espe.drivex.exception.ResourceNotFoundException;
import com.espe.drivex.repository.MetodoPagoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MetodoPagoService {

    @Autowired
    private MetodoPagoRepository metodoPagoRepository;

    public List<MetodoPago> listarTodos() {
        return metodoPagoRepository.findAll().stream()
                .filter(MetodoPago::getActivo)
                .collect(Collectors.toList());
    }

    public MetodoPago buscarPorId(Long id) {
        return metodoPagoRepository.findById(id)
                .filter(MetodoPago::getActivo)
                .orElseThrow(() -> new ResourceNotFoundException("MetodoPago no encontrado con id: " + id));
    }

    public MetodoPago guardar(MetodoPago entidad) {
        validar(entidad);
        entidad.setActivo(true);
        return metodoPagoRepository.save(entidad);
    }

    public MetodoPago actualizar(Long id, MetodoPago datos) {
        MetodoPago existente = buscarPorId(id);
        validar(datos);
        existente.setNombre(datos.getNombre());
        return metodoPagoRepository.save(existente);
    }

    public void eliminar(Long id) {
        MetodoPago existente = buscarPorId(id);
        existente.setActivo(false);
        metodoPagoRepository.save(existente);
    }

    private void validar(MetodoPago entidad) {
        if (entidad.getNombre() == null || entidad.getNombre().isBlank()) {
            throw new RuntimeException("El nombre es obligatorio");
        }
    }
}
