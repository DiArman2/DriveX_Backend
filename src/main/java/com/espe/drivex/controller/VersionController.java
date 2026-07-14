package com.espe.drivex.controller;

import com.espe.drivex.entity.Version;
import com.espe.drivex.service.VersionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/versiones")
public class VersionController {

    @Autowired
    private VersionService versionService;

    @GetMapping
    public ResponseEntity<List<Version>> listar() {
        return ResponseEntity.ok(versionService.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Version> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(versionService.buscarPorId(id));
    }

    @PostMapping
    public ResponseEntity<Version> crear(@RequestBody Version version) {
        Version creada = versionService.guardar(version);
        return ResponseEntity.status(HttpStatus.CREATED).body(creada);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Version> actualizar(@PathVariable Long id, @RequestBody Version version) {
        return ResponseEntity.ok(versionService.actualizar(id, version));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        versionService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
