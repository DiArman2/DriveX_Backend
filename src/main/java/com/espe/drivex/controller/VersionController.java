package com.espe.drivex.controller;

import com.espe.drivex.dto.VersionRequest;
import com.espe.drivex.dto.VersionResponse;
import com.espe.drivex.entity.Modelo;
import com.espe.drivex.entity.Version;
import com.espe.drivex.service.VersionService;
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
@RequestMapping("/api/versiones")
@Tag(name = "Versiones", description = "Gestión de versiones de modelos de vehículos")
@SecurityRequirement(name = "basicAuth")
public class VersionController {

    private final VersionService versionService;

    public VersionController(VersionService versionService) {
        this.versionService = versionService;
    }

    @GetMapping
    @Operation(summary = "Listar todas las versiones activas")
    public ResponseEntity<List<VersionResponse>> listar() {
        List<VersionResponse> lista = versionService.listarTodos()
                .stream().map(VersionResponse::fromEntity).collect(Collectors.toList());
        return ResponseEntity.ok(lista);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar versión por ID")
    public ResponseEntity<VersionResponse> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(VersionResponse.fromEntity(versionService.buscarPorId(id)));
    }

    @PostMapping
    @Operation(summary = "Registrar nueva versión")
    public ResponseEntity<VersionResponse> crear(@Valid @RequestBody VersionRequest request) {
        Version entidad = new Version();
        entidad.setNombre(request.getNombre());
        entidad.setAnio(request.getAnio());
        Modelo modelo = new Modelo();
        modelo.setId(request.getModeloId());
        entidad.setModelo(modelo);
        Version creada = versionService.guardar(entidad);
        return ResponseEntity.status(HttpStatus.CREATED).body(VersionResponse.fromEntity(creada));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar versión existente")
    public ResponseEntity<VersionResponse> actualizar(@PathVariable Long id,
                                                       @Valid @RequestBody VersionRequest request) {
        Version entidad = new Version();
        entidad.setNombre(request.getNombre());
        entidad.setAnio(request.getAnio());
        Modelo modelo = new Modelo();
        modelo.setId(request.getModeloId());
        entidad.setModelo(modelo);
        Version actualizada = versionService.actualizar(id, entidad);
        return ResponseEntity.ok(VersionResponse.fromEntity(actualizada));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Desactivar versión (eliminación lógica)")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        versionService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
