package com.espe.drivex.controller;

import com.espe.drivex.dto.VehiculoRequest;
import com.espe.drivex.dto.VehiculoResponse;
import com.espe.drivex.service.VehiculoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/vehiculos")
@Tag(name = "Vehículos", description = "Gestión del catálogo de vehículos")
@SecurityRequirement(name = "basicAuth")
public class VehiculoController {

    private final VehiculoService vehiculoService;

    public VehiculoController(VehiculoService vehiculoService) {
        this.vehiculoService = vehiculoService;
    }

    @GetMapping
    @Operation(summary = "Listar todos los vehículos activos")
    public ResponseEntity<List<VehiculoResponse>> listar() {
        return ResponseEntity.ok(vehiculoService.listarTodos());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar vehículo por ID")
    public ResponseEntity<VehiculoResponse> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(vehiculoService.buscarPorId(id));
    }

    @GetMapping("/disponibles")
    @Operation(summary = "Listar vehículos disponibles para alquiler")
    public ResponseEntity<List<VehiculoResponse>> listarDisponibles() {
        return ResponseEntity.ok(vehiculoService.listarDisponibles());
    }

    @GetMapping("/buscar")
    @Operation(summary = "Buscar vehículos por color")
    public ResponseEntity<List<VehiculoResponse>> buscarPorColor(@RequestParam String color) {
        return ResponseEntity.ok(vehiculoService.buscarPorColor(color));
    }

    @PostMapping
    @Operation(summary = "Registrar nuevo vehículo")
    public ResponseEntity<VehiculoResponse> crear(@Valid @RequestBody VehiculoRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(vehiculoService.guardar(request));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar vehículo existente")
    public ResponseEntity<VehiculoResponse> actualizar(@PathVariable Long id,
                                                        @Valid @RequestBody VehiculoRequest request) {
        return ResponseEntity.ok(vehiculoService.actualizar(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Desactivar vehículo (eliminación lógica)")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        vehiculoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
