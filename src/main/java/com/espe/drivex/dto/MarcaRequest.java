package com.espe.drivex.dto;

import jakarta.validation.constraints.NotBlank;

public class MarcaRequest {

    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;

    public MarcaRequest() {
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}
