package com.espe.drivex.service;

import com.espe.drivex.entity.Rol;
import com.espe.drivex.exception.ResourceNotFoundException;
import com.espe.drivex.repository.RolRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RolService {

    @Autowired
    private RolRepository rolRepository;

    public List<Rol> listarTodos() {
        return rolRepository.findAll().stream()
                .filter(Rol::getActivo)
                .collect(Collectors.toList());
    }

    public Rol buscarPorId(Long id) {
        return rolRepository.findById(id)
                .filter(Rol::getActivo)
                .orElseThrow(() -> new ResourceNotFoundException("Rol no encontrado con id: " + id));
    }

    public Rol guardar(Rol rol) {
        validar(rol);
        rol.setActivo(true);
        rol.setFechaCreacion(LocalDateTime.now());
        return rolRepository.save(rol);
    }

    public Rol actualizar(Long id, Rol datos) {
        Rol existente = buscarPorId(id);
        validar(datos);
        existente.setNombre(datos.getNombre());
        existente.setDescripcion(datos.getDescripcion());
        return rolRepository.save(existente);
    }

    public void eliminar(Long id) {
        Rol existente = buscarPorId(id);
        existente.setActivo(false);
        rolRepository.save(existente);
    }

    private void validar(Rol rol) {
        if (rol.getNombre() == null || rol.getNombre().isBlank()) {
            throw new RuntimeException("El nombre del rol es obligatorio");
        }
    }
}
