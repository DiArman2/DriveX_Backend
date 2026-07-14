package com.espe.drivex.dto;

public class EstadoPagoResponse {

    private Long id;
    private String nombre;
    private Boolean activo;

    public EstadoPagoResponse() {
    }

    public EstadoPagoResponse(Long id, String nombre, Boolean activo) {
        this.id = id;
        this.nombre = nombre;
        this.activo = activo;
    }

    public static EstadoPagoResponse fromEntity(com.espe.drivex.entity.EstadoPago e) {
        return new EstadoPagoResponse(e.getId(), e.getNombre(), e.getActivo());
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }
}
