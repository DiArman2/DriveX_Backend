package com.espe.drivex.controller;

import com.espe.drivex.dto.RolRequest;
import com.espe.drivex.dto.RolResponse;
import com.espe.drivex.entity.Rol;
import com.espe.drivex.service.RolService;
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
@RequestMapping("/api/roles")
@Tag(name = "Roles", description = "Catálogo de roles del sistema")
@SecurityRequirement(name = "basicAuth")
public class RolController {

    private final RolService rolService;

    public RolController(RolService rolService) {
        this.rolService = rolService;
    }

    @GetMapping
    @Operation(summary = "Listar todos los roles activos")
    public ResponseEntity<List<RolResponse>> listar() {
        List<RolResponse> lista = rolService.listarTodos()
                .stream().map(RolResponse::fromEntity).collect(Collectors.toList());
        return ResponseEntity.ok(lista);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar rol por ID")
    public ResponseEntity<RolResponse> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(RolResponse.fromEntity(rolService.buscarPorId(id)));
    }

    @PostMapping
    @Operation(summary = "Registrar nuevo rol")
    public ResponseEntity<RolResponse> crear(@Valid @RequestBody RolRequest request) {
        Rol entidad = new Rol();
        entidad.setNombre(request.getNombre());
        entidad.setDescripcion(request.getDescripcion());
        Rol creado = rolService.guardar(entidad);
        return ResponseEntity.status(HttpStatus.CREATED).body(RolResponse.fromEntity(creado));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar rol existente")
    public ResponseEntity<RolResponse> actualizar(@PathVariable Long id,
                                                   @Valid @RequestBody RolRequest request) {
        Rol entidad = new Rol();
        entidad.setNombre(request.getNombre());
        entidad.setDescripcion(request.getDescripcion());
        Rol actualizado = rolService.actualizar(id, entidad);
        return ResponseEntity.ok(RolResponse.fromEntity(actualizado));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Desactivar rol (eliminación lógica)")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        rolService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
