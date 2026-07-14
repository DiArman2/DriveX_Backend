package com.espe.drivex.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class ModeloRequest {

    @NotNull(message = "El id de la marca es obligatorio")
    private Long marcaId;

    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;

    public ModeloRequest() {
    }

    public Long getMarcaId() {
        return marcaId;
    }

    public void setMarcaId(Long marcaId) {
        this.marcaId = marcaId;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}
