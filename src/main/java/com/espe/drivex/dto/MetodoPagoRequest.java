package com.espe.drivex.dto;

import jakarta.validation.constraints.NotBlank;

public class MetodoPagoRequest {

    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;

    public MetodoPagoRequest() {
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}
