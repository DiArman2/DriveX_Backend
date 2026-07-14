package com.espe.drivex.dto;

import jakarta.validation.constraints.NotBlank;

public class EstadoPagoRequest {

    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;

    public EstadoPagoRequest() {
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}
