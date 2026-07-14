package com.espe.drivex.controller;

import com.espe.drivex.dto.MetodoPagoRequest;
import com.espe.drivex.dto.MetodoPagoResponse;
import com.espe.drivex.entity.MetodoPago;
import com.espe.drivex.service.MetodoPagoService;
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
@RequestMapping("/api/metodos-pago")
@Tag(name = "Métodos de Pago", description = "Catálogo de métodos de pago")
@SecurityRequirement(name = "basicAuth")
public class MetodoPagoController {

    private final MetodoPagoService metodoPagoService;

    public MetodoPagoController(MetodoPagoService metodoPagoService) {
        this.metodoPagoService = metodoPagoService;
    }

    @GetMapping
    @Operation(summary = "Listar todos los métodos de pago activos")
    public ResponseEntity<List<MetodoPagoResponse>> listar() {
        List<MetodoPagoResponse> lista = metodoPagoService.listarTodos()
                .stream().map(MetodoPagoResponse::fromEntity).collect(Collectors.toList());
        return ResponseEntity.ok(lista);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar método de pago por ID")
    public ResponseEntity<MetodoPagoResponse> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(MetodoPagoResponse.fromEntity(metodoPagoService.buscarPorId(id)));
    }

    @PostMapping
    @Operation(summary = "Registrar nuevo método de pago")
    public ResponseEntity<MetodoPagoResponse> crear(@Valid @RequestBody MetodoPagoRequest request) {
        MetodoPago entidad = new MetodoPago();
        entidad.setNombre(request.getNombre());
        MetodoPago creado = metodoPagoService.guardar(entidad);
        return ResponseEntity.status(HttpStatus.CREATED).body(MetodoPagoResponse.fromEntity(creado));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar método de pago existente")
    public ResponseEntity<MetodoPagoResponse> actualizar(@PathVariable Long id,
                                                          @Valid @RequestBody MetodoPagoRequest request) {
        MetodoPago entidad = new MetodoPago();
        entidad.setNombre(request.getNombre());
        MetodoPago actualizado = metodoPagoService.actualizar(id, entidad);
        return ResponseEntity.ok(MetodoPagoResponse.fromEntity(actualizado));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Desactivar método de pago (eliminación lógica)")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        metodoPagoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
