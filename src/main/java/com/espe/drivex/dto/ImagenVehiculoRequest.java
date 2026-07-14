package com.espe.drivex.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class ImagenVehiculoRequest {

    @NotNull(message = "El id del vehículo es obligatorio")
    private Long vehiculoId;

    @NotBlank(message = "La URL es obligatoria")
    private String url;

    private Boolean esPrincipal = false;

    public ImagenVehiculoRequest() {
    }

    public Long getVehiculoId() {
        return vehiculoId;
    }

    public void setVehiculoId(Long vehiculoId) {
        this.vehiculoId = vehiculoId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Boolean getEsPrincipal() {
        return esPrincipal;
    }

    public void setEsPrincipal(Boolean esPrincipal) {
        this.esPrincipal = esPrincipal;
    }
}
