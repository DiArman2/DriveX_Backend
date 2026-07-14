package com.espe.drivex.controller;

import com.espe.drivex.dto.ImagenVehiculoRequest;
import com.espe.drivex.dto.ImagenVehiculoResponse;
import com.espe.drivex.entity.ImagenVehiculo;
import com.espe.drivex.entity.Vehiculo;
import com.espe.drivex.service.ImagenVehiculoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/imagenes-vehiculo")
@Tag(name = "Imágenes de Vehículo", description = "Gestión de imágenes asociadas a vehículos")
@SecurityRequirement(name = "basicAuth")
public class ImagenVehiculoController {

    private final ImagenVehiculoService imagenVehiculoService;

    public ImagenVehiculoController(ImagenVehiculoService imagenVehiculoService) {
        this.imagenVehiculoService = imagenVehiculoService;
    }

    @GetMapping
    @Operation(summary = "Listar todas las imágenes activas")
    public ResponseEntity<List<ImagenVehiculoResponse>> listar() {
        List<ImagenVehiculoResponse> lista = imagenVehiculoService.listarTodos()
                .stream().map(ImagenVehiculoResponse::fromEntity).collect(Collectors.toList());
        return ResponseEntity.ok(lista);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar imagen por ID")
    public ResponseEntity<ImagenVehiculoResponse> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(ImagenVehiculoResponse.fromEntity(imagenVehiculoService.buscarPorId(id)));
    }

    @PostMapping
    @Operation(summary = "Registrar nueva imagen de vehículo")
    public ResponseEntity<ImagenVehiculoResponse> crear(@Valid @RequestBody ImagenVehiculoRequest request) {
        ImagenVehiculo entidad = new ImagenVehiculo();
        entidad.setUrl(request.getUrl());
        entidad.setEsPrincipal(request.getEsPrincipal());
        Vehiculo vehiculo = new Vehiculo();
        vehiculo.setId(request.getVehiculoId());
        entidad.setVehiculo(vehiculo);
        ImagenVehiculo creada = imagenVehiculoService.guardar(entidad);
        return ResponseEntity.status(HttpStatus.CREATED).body(ImagenVehiculoResponse.fromEntity(creada));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar imagen existente")
    public ResponseEntity<ImagenVehiculoResponse> actualizar(@PathVariable Long id,
                                                              @Valid @RequestBody ImagenVehiculoRequest request) {
        ImagenVehiculo entidad = new ImagenVehiculo();
        entidad.setUrl(request.getUrl());
        entidad.setEsPrincipal(request.getEsPrincipal());
        Vehiculo vehiculo = new Vehiculo();
        vehiculo.setId(request.getVehiculoId());
        entidad.setVehiculo(vehiculo);
        ImagenVehiculo actualizada = imagenVehiculoService.actualizar(id, entidad);
        return ResponseEntity.ok(ImagenVehiculoResponse.fromEntity(actualizada));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Desactivar imagen (eliminación lógica)")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        imagenVehiculoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
