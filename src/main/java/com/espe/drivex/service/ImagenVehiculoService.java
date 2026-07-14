package com.espe.drivex.service;

import com.espe.drivex.entity.ImagenVehiculo;
import com.espe.drivex.exception.ResourceNotFoundException;
import com.espe.drivex.repository.ImagenVehiculoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ImagenVehiculoService {

    @Autowired
    private ImagenVehiculoRepository imagenVehiculoRepository;

    public List<ImagenVehiculo> listarTodos() {
        return imagenVehiculoRepository.findAll().stream()
                .filter(ImagenVehiculo::getActivo)
                .collect(Collectors.toList());
    }

    public ImagenVehiculo buscarPorId(Long id) {
        return imagenVehiculoRepository.findById(id)
                .filter(ImagenVehiculo::getActivo)
                .orElseThrow(() -> new ResourceNotFoundException("Imagen no encontrada con id: " + id));
    }

    public ImagenVehiculo guardar(ImagenVehiculo imagen) {
        validar(imagen);
        imagen.setActivo(true);
        imagen.setFechaCreacion(LocalDateTime.now());
        if (imagen.getEsPrincipal() == null) {
            imagen.setEsPrincipal(false);
        }
        return imagenVehiculoRepository.save(imagen);
    }

    public ImagenVehiculo actualizar(Long id, ImagenVehiculo datosActualizados) {
        ImagenVehiculo imagen = buscarPorId(id);
        validar(datosActualizados);
        imagen.setUrl(datosActualizados.getUrl());
        imagen.setVehiculo(datosActualizados.getVehiculo());
        imagen.setEsPrincipal(datosActualizados.getEsPrincipal());
        return imagenVehiculoRepository.save(imagen);
    }

    public void eliminar(Long id) {
        ImagenVehiculo imagen = buscarPorId(id);
        imagen.setActivo(false);
        imagenVehiculoRepository.save(imagen);
    }

    private void validar(ImagenVehiculo imagen) {
        if (imagen.getUrl() == null || imagen.getUrl().isBlank()) {
            throw new RuntimeException("La URL de la imagen es obligatoria");
        }
        if (imagen.getVehiculo() == null) {
            throw new RuntimeException("El vehiculo es obligatorio");
        }
    }
}
