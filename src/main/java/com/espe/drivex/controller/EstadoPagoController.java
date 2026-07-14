package com.espe.drivex.controller;

import com.espe.drivex.dto.EstadoPagoRequest;
import com.espe.drivex.dto.EstadoPagoResponse;
import com.espe.drivex.entity.EstadoPago;
import com.espe.drivex.service.EstadoPagoService;
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
@RequestMapping("/api/estados-pago")
@Tag(name = "Estados de Pago", description = "Catálogo de estados de pago")
@SecurityRequirement(name = "basicAuth")
public class EstadoPagoController {

    private final EstadoPagoService estadoPagoService;

    public EstadoPagoController(EstadoPagoService estadoPagoService) {
        this.estadoPagoService = estadoPagoService;
    }

    @GetMapping
    @Operation(summary = "Listar todos los estados de pago activos")
    public ResponseEntity<List<EstadoPagoResponse>> listar() {
        List<EstadoPagoResponse> lista = estadoPagoService.listarTodos()
                .stream().map(EstadoPagoResponse::fromEntity).collect(Collectors.toList());
        return ResponseEntity.ok(lista);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar estado de pago por ID")
    public ResponseEntity<EstadoPagoResponse> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(EstadoPagoResponse.fromEntity(estadoPagoService.buscarPorId(id)));
    }

    @PostMapping
    @Operation(summary = "Registrar nuevo estado de pago")
    public ResponseEntity<EstadoPagoResponse> crear(@Valid @RequestBody EstadoPagoRequest request) {
        EstadoPago entidad = new EstadoPago();
        entidad.setNombre(request.getNombre());
        EstadoPago creado = estadoPagoService.guardar(entidad);
        return ResponseEntity.status(HttpStatus.CREATED).body(EstadoPagoResponse.fromEntity(creado));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar estado de pago existente")
    public ResponseEntity<EstadoPagoResponse> actualizar(@PathVariable Long id,
                                                          @Valid @RequestBody EstadoPagoRequest request) {
        EstadoPago entidad = new EstadoPago();
        entidad.setNombre(request.getNombre());
        EstadoPago actualizado = estadoPagoService.actualizar(id, entidad);
        return ResponseEntity.ok(EstadoPagoResponse.fromEntity(actualizado));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Desactivar estado de pago (eliminación lógica)")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        estadoPagoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
