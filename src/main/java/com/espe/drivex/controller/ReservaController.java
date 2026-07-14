package com.espe.drivex.controller;

import com.espe.drivex.dto.ReservaRequest;
import com.espe.drivex.dto.ReservaResponse;
import com.espe.drivex.service.ReservaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reservas")
@Tag(name = "Reservas", description = "Gestión de reservas de vehículos (proceso transaccional)")
@SecurityRequirement(name = "basicAuth")
public class ReservaController {

    private final ReservaService reservaService;

    public ReservaController(ReservaService reservaService) {
        this.reservaService = reservaService;
    }

    @GetMapping
    @Operation(summary = "Listar todas las reservas activas")
    public ResponseEntity<List<ReservaResponse>> listar() {
        return ResponseEntity.ok(reservaService.listarTodos());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar reserva por ID")
    public ResponseEntity<ReservaResponse> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(reservaService.buscarPorId(id));
    }

    @GetMapping("/cliente/{clienteId}")
    @Operation(summary = "Listar reservas de un cliente")
    public ResponseEntity<List<ReservaResponse>> buscarPorCliente(@PathVariable Long clienteId) {
        return ResponseEntity.ok(reservaService.buscarPorCliente(clienteId));
    }

    @PostMapping
    @Operation(summary = "Registrar reserva y actualizar disponibilidad del vehículo (@Transactional)")
    public ResponseEntity<ReservaResponse> crear(@Valid @RequestBody ReservaRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(reservaService.guardar(request));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar reserva existente")
    public ResponseEntity<ReservaResponse> actualizar(@PathVariable Long id,
                                                       @Valid @RequestBody ReservaRequest request) {
        return ResponseEntity.ok(reservaService.actualizar(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Cancelar reserva y liberar vehículo (eliminación lógica)")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        reservaService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
