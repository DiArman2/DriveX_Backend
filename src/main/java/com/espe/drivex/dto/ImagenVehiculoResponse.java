package com.espe.drivex.dto;

import java.time.LocalDateTime;

public class ImagenVehiculoResponse {

    private Long id;
    private Long vehiculoId;
    private String vehiculoPlaca;
    private String url;
    private Boolean esPrincipal;
    private Boolean activo;
    private LocalDateTime fechaCreacion;

    public ImagenVehiculoResponse() {
    }

    public ImagenVehiculoResponse(Long id, Long vehiculoId, String vehiculoPlaca, String url,
                                   Boolean esPrincipal, Boolean activo, LocalDateTime fechaCreacion) {
        this.id = id;
        this.vehiculoId = vehiculoId;
        this.vehiculoPlaca = vehiculoPlaca;
        this.url = url;
        this.esPrincipal = esPrincipal;
        this.activo = activo;
        this.fechaCreacion = fechaCreacion;
    }

    public static ImagenVehiculoResponse fromEntity(com.espe.drivex.entity.ImagenVehiculo e) {
        return new ImagenVehiculoResponse(
                e.getId(),
                e.getVehiculo().getId(),
                e.getVehiculo().getPlaca(),
                e.getUrl(),
                e.getEsPrincipal(),
                e.getActivo(),
                e.getFechaCreacion()
        );
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getVehiculoId() {
        return vehiculoId;
    }

    public void setVehiculoId(Long vehiculoId) {
        this.vehiculoId = vehiculoId;
    }

    public String getVehiculoPlaca() {
        return vehiculoPlaca;
    }

    public void setVehiculoPlaca(String vehiculoPlaca) {
        this.vehiculoPlaca = vehiculoPlaca;
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

    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }
}
