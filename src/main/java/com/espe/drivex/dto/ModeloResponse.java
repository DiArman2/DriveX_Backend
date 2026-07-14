package com.espe.drivex.dto;

import java.time.LocalDateTime;

public class ModeloResponse {

    private Long id;
    private Long marcaId;
    private String marcaNombre;
    private String nombre;
    private Boolean activo;
    private LocalDateTime fechaCreacion;

    public ModeloResponse() {
    }

    public ModeloResponse(Long id, Long marcaId, String marcaNombre, String nombre,
                           Boolean activo, LocalDateTime fechaCreacion) {
        this.id = id;
        this.marcaId = marcaId;
        this.marcaNombre = marcaNombre;
        this.nombre = nombre;
        this.activo = activo;
        this.fechaCreacion = fechaCreacion;
    }

    public static ModeloResponse fromEntity(com.espe.drivex.entity.Modelo e) {
        return new ModeloResponse(
                e.getId(),
                e.getMarca().getId(),
                e.getMarca().getNombre(),
                e.getNombre(),
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

    public Long getMarcaId() {
        return marcaId;
    }

    public void setMarcaId(Long marcaId) {
        this.marcaId = marcaId;
    }

    public String getMarcaNombre() {
        return marcaNombre;
    }

    public void setMarcaNombre(String marcaNombre) {
        this.marcaNombre = marcaNombre;
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

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }
}
