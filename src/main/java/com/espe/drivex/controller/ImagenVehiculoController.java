package com.espe.drivex.controller;

import com.espe.drivex.entity.ImagenVehiculo;
import com.espe.drivex.service.ImagenVehiculoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/imagenes-vehiculo")
public class ImagenVehiculoController {

    @Autowired
    private ImagenVehiculoService imagenVehiculoService;

    @GetMapping
    public ResponseEntity<List<ImagenVehiculo>> listar() {
        return ResponseEntity.ok(imagenVehiculoService.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ImagenVehiculo> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(imagenVehiculoService.buscarPorId(id));
    }

    @PostMapping
    public ResponseEntity<ImagenVehiculo> crear(@RequestBody ImagenVehiculo imagen) {
        ImagenVehiculo creada = imagenVehiculoService.guardar(imagen);
        return ResponseEntity.status(HttpStatus.CREATED).body(creada);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ImagenVehiculo> actualizar(@PathVariable Long id, @RequestBody ImagenVehiculo imagen) {
        return ResponseEntity.ok(imagenVehiculoService.actualizar(id, imagen));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        imagenVehiculoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
