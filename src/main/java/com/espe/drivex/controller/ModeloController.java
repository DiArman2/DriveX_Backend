package com.espe.drivex.controller;

import com.espe.drivex.entity.Modelo;
import com.espe.drivex.service.ModeloService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/modelos")
public class ModeloController {

    @Autowired
    private ModeloService modeloService;

    @GetMapping
    public ResponseEntity<List<Modelo>> listar() {
        return ResponseEntity.ok(modeloService.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Modelo> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(modeloService.buscarPorId(id));
    }

    @PostMapping
    public ResponseEntity<Modelo> crear(@RequestBody Modelo modelo) {
        Modelo creado = modeloService.guardar(modelo);
        return ResponseEntity.status(HttpStatus.CREATED).body(creado);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Modelo> actualizar(@PathVariable Long id, @RequestBody Modelo modelo) {
        return ResponseEntity.ok(modeloService.actualizar(id, modelo));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        modeloService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
