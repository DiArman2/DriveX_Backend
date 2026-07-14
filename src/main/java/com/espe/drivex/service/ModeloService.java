package com.espe.drivex.service;

import com.espe.drivex.entity.Modelo;
import com.espe.drivex.exception.ResourceNotFoundException;
import com.espe.drivex.repository.ModeloRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ModeloService {

    @Autowired
    private ModeloRepository modeloRepository;

    public List<Modelo> listarTodos() {
        return modeloRepository.findAll().stream()
                .filter(Modelo::getActivo)
                .collect(Collectors.toList());
    }

    public Modelo buscarPorId(Long id) {
        return modeloRepository.findById(id)
                .filter(Modelo::getActivo)
                .orElseThrow(() -> new ResourceNotFoundException("Modelo no encontrado con id: " + id));
    }

    public Modelo guardar(Modelo modelo) {
        validar(modelo);
        modelo.setActivo(true);
        modelo.setFechaCreacion(LocalDateTime.now());
        return modeloRepository.save(modelo);
    }

    public Modelo actualizar(Long id, Modelo datosActualizados) {
        Modelo modelo = buscarPorId(id);
        validar(datosActualizados);
        modelo.setNombre(datosActualizados.getNombre());
        modelo.setMarca(datosActualizados.getMarca());
        return modeloRepository.save(modelo);
    }

    public void eliminar(Long id) {
        Modelo modelo = buscarPorId(id);
        modelo.setActivo(false);
        modeloRepository.save(modelo);
    }

    private void validar(Modelo modelo) {
        if (modelo.getNombre() == null || modelo.getNombre().isBlank()) {
            throw new RuntimeException("El nombre del modelo es obligatorio");
        }
        if (modelo.getMarca() == null) {
            throw new RuntimeException("La marca es obligatoria");
        }
    }
}
