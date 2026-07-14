package com.espe.drivex.controller;

import com.espe.drivex.dto.MarcaRequest;
import com.espe.drivex.dto.MarcaResponse;
import com.espe.drivex.service.MarcaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/marcas")
@Tag(name = "Marcas", description = "Gestión de marcas de vehículos")
@SecurityRequirement(name = "basicAuth")
public class MarcaController {

    private final MarcaService marcaService;

    public MarcaController(MarcaService marcaService) {
        this.marcaService = marcaService;
    }

    @GetMapping
    @Operation(summary = "Listar todas las marcas activas")
    public ResponseEntity<List<MarcaResponse>> listar() {
        return ResponseEntity.ok(marcaService.listarTodos());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar marca por ID")
    public ResponseEntity<MarcaResponse> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(marcaService.buscarPorId(id));
    }

    @GetMapping("/buscar")
    @Operation(summary = "Buscar marcas por nombre")
    public ResponseEntity<List<MarcaResponse>> buscarPorNombre(@RequestParam String nombre) {
        return ResponseEntity.ok(marcaService.buscarPorNombre(nombre));
    }

    @PostMapping
    @Operation(summary = "Registrar nueva marca")
    public ResponseEntity<MarcaResponse> crear(@Valid @RequestBody MarcaRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(marcaService.guardar(request));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar marca existente")
    public ResponseEntity<MarcaResponse> actualizar(@PathVariable Long id,
                                                     @Valid @RequestBody MarcaRequest request) {
        return ResponseEntity.ok(marcaService.actualizar(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Desactivar marca (eliminación lógica)")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        marcaService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
