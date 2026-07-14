package com.espe.drivex.dto;

import jakarta.validation.constraints.NotBlank;

public class EstadoReservaRequest {

    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;

    public EstadoReservaRequest() {
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}
