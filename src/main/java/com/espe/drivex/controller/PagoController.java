package com.espe.drivex.controller;

import com.espe.drivex.dto.PagoRequest;
import com.espe.drivex.dto.PagoResponse;
import com.espe.drivex.service.PagoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pagos")
@Tag(name = "Pagos", description = "Gestión de pagos vinculados a reservas")
@SecurityRequirement(name = "basicAuth")
public class PagoController {

    private final PagoService pagoService;

    public PagoController(PagoService pagoService) {
        this.pagoService = pagoService;
    }

    @GetMapping
    @Operation(summary = "Listar todos los pagos activos")
    public ResponseEntity<List<PagoResponse>> listar() {
        return ResponseEntity.ok(pagoService.listarTodos());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar pago por ID")
    public ResponseEntity<PagoResponse> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(pagoService.buscarPorId(id));
    }

    @GetMapping("/reserva/{reservaId}")
    @Operation(summary = "Listar pagos de una reserva")
    public ResponseEntity<List<PagoResponse>> buscarPorReserva(@PathVariable Long reservaId) {
        return ResponseEntity.ok(pagoService.buscarPorReserva(reservaId));
    }

    @PostMapping
    @Operation(summary = "Registrar nuevo pago (@Transactional, valida monto contra reserva)")
    public ResponseEntity<PagoResponse> crear(@Valid @RequestBody PagoRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(pagoService.guardar(request));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar pago existente")
    public ResponseEntity<PagoResponse> actualizar(@PathVariable Long id,
                                                    @Valid @RequestBody PagoRequest request) {
        return ResponseEntity.ok(pagoService.actualizar(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Desactivar pago (eliminación lógica)")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        pagoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
