package com.espe.drivex.controller;

import com.espe.drivex.dto.EstadoReservaRequest;
import com.espe.drivex.dto.EstadoReservaResponse;
import com.espe.drivex.entity.EstadoReserva;
import com.espe.drivex.service.EstadoReservaService;
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
@RequestMapping("/api/estados-reserva")
@Tag(name = "Estados de Reserva", description = "Catálogo de estados de reserva")
@SecurityRequirement(name = "basicAuth")
public class EstadoReservaController {

    private final EstadoReservaService estadoReservaService;

    public EstadoReservaController(EstadoReservaService estadoReservaService) {
        this.estadoReservaService = estadoReservaService;
    }

    @GetMapping
    @Operation(summary = "Listar todos los estados de reserva activos")
    public ResponseEntity<List<EstadoReservaResponse>> listar() {
        List<EstadoReservaResponse> lista = estadoReservaService.listarTodos()
                .stream().map(EstadoReservaResponse::fromEntity).collect(Collectors.toList());
        return ResponseEntity.ok(lista);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar estado de reserva por ID")
    public ResponseEntity<EstadoReservaResponse> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(EstadoReservaResponse.fromEntity(estadoReservaService.buscarPorId(id)));
    }

    @PostMapping
    @Operation(summary = "Registrar nuevo estado de reserva")
    public ResponseEntity<EstadoReservaResponse> crear(@Valid @RequestBody EstadoReservaRequest request) {
        EstadoReserva entidad = new EstadoReserva();
        entidad.setNombre(request.getNombre());
        EstadoReserva creado = estadoReservaService.guardar(entidad);
        return ResponseEntity.status(HttpStatus.CREATED).body(EstadoReservaResponse.fromEntity(creado));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar estado de reserva existente")
    public ResponseEntity<EstadoReservaResponse> actualizar(@PathVariable Long id,
                                                             @Valid @RequestBody EstadoReservaRequest request) {
        EstadoReserva entidad = new EstadoReserva();
        entidad.setNombre(request.getNombre());
        EstadoReserva actualizado = estadoReservaService.actualizar(id, entidad);
        return ResponseEntity.ok(EstadoReservaResponse.fromEntity(actualizado));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Desactivar estado de reserva (eliminación lógica)")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        estadoReservaService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
