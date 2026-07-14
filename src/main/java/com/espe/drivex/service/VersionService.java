package com.espe.drivex.service;

import com.espe.drivex.entity.Version;
import com.espe.drivex.exception.ResourceNotFoundException;
import com.espe.drivex.repository.VersionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class VersionService {

    @Autowired
    private VersionRepository versionRepository;

    public List<Version> listarTodos() {
        return versionRepository.findAll().stream()
                .filter(Version::getActivo)
                .collect(Collectors.toList());
    }

    public Version buscarPorId(Long id) {
        return versionRepository.findById(id)
                .filter(Version::getActivo)
                .orElseThrow(() -> new ResourceNotFoundException("Version no encontrada con id: " + id));
    }

    public Version guardar(Version version) {
        validar(version);
        version.setActivo(true);
        version.setFechaCreacion(LocalDateTime.now());
        return versionRepository.save(version);
    }

    public Version actualizar(Long id, Version datosActualizados) {
        Version version = buscarPorId(id);
        validar(datosActualizados);
        version.setNombre(datosActualizados.getNombre());
        version.setAnio(datosActualizados.getAnio());
        version.setModelo(datosActualizados.getModelo());
        return versionRepository.save(version);
    }

    public void eliminar(Long id) {
        Version version = buscarPorId(id);
        version.setActivo(false);
        versionRepository.save(version);
    }

    private void validar(Version version) {
        if (version.getNombre() == null || version.getNombre().isBlank()) {
            throw new RuntimeException("El nombre de la version es obligatorio");
        }
        if (version.getModelo() == null) {
            throw new RuntimeException("El modelo es obligatorio");
        }
        if (version.getAnio() == null || version.getAnio() < 1990 || version.getAnio() > 2100) {
            throw new RuntimeException("El anio debe estar entre 1990 y 2100");
        }
    }
}
