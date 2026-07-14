package com.espe.drivex.controller;

import com.espe.drivex.dto.ModeloRequest;
import com.espe.drivex.dto.ModeloResponse;
import com.espe.drivex.entity.Marca;
import com.espe.drivex.entity.Modelo;
import com.espe.drivex.service.ModeloService;
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
@RequestMapping("/api/modelos")
@Tag(name = "Modelos", description = "Gestión de modelos de vehículos")
@SecurityRequirement(name = "basicAuth")
public class ModeloController {

    private final ModeloService modeloService;

    public ModeloController(ModeloService modeloService) {
        this.modeloService = modeloService;
    }

    @GetMapping
    @Operation(summary = "Listar todos los modelos activos")
    public ResponseEntity<List<ModeloResponse>> listar() {
        List<ModeloResponse> lista = modeloService.listarTodos()
                .stream().map(ModeloResponse::fromEntity).collect(Collectors.toList());
        return ResponseEntity.ok(lista);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar modelo por ID")
    public ResponseEntity<ModeloResponse> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(ModeloResponse.fromEntity(modeloService.buscarPorId(id)));
    }

    @PostMapping
    @Operation(summary = "Registrar nuevo modelo")
    public ResponseEntity<ModeloResponse> crear(@Valid @RequestBody ModeloRequest request) {
        Modelo entidad = new Modelo();
        entidad.setNombre(request.getNombre());
        Marca marca = new Marca();
        marca.setId(request.getMarcaId());
        entidad.setMarca(marca);
        Modelo creado = modeloService.guardar(entidad);
        return ResponseEntity.status(HttpStatus.CREATED).body(ModeloResponse.fromEntity(creado));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar modelo existente")
    public ResponseEntity<ModeloResponse> actualizar(@PathVariable Long id,
                                                      @Valid @RequestBody ModeloRequest request) {
        Modelo entidad = new Modelo();
        entidad.setNombre(request.getNombre());
        Marca marca = new Marca();
        marca.setId(request.getMarcaId());
        entidad.setMarca(marca);
        Modelo actualizado = modeloService.actualizar(id, entidad);
        return ResponseEntity.ok(ModeloResponse.fromEntity(actualizado));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Desactivar modelo (eliminación lógica)")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        modeloService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
