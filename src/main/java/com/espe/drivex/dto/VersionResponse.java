package com.espe.drivex.dto;

import java.time.LocalDateTime;

public class VersionResponse {

    private Long id;
    private Long modeloId;
    private String modeloNombre;
    private Long marcaId;
    private String marcaNombre;
    private String nombre;
    private Integer anio;
    private Boolean activo;
    private LocalDateTime fechaCreacion;

    public VersionResponse() {
    }

    public VersionResponse(Long id, Long modeloId, String modeloNombre, Long marcaId, String marcaNombre,
                            String nombre, Integer anio, Boolean activo, LocalDateTime fechaCreacion) {
        this.id = id;
        this.modeloId = modeloId;
        this.modeloNombre = modeloNombre;
        this.marcaId = marcaId;
        this.marcaNombre = marcaNombre;
        this.nombre = nombre;
        this.anio = anio;
        this.activo = activo;
        this.fechaCreacion = fechaCreacion;
    }

    public static VersionResponse fromEntity(com.espe.drivex.entity.Version e) {
        return new VersionResponse(
                e.getId(),
                e.getModelo().getId(),
                e.getModelo().getNombre(),
                e.getModelo().getMarca().getId(),
                e.getModelo().getMarca().getNombre(),
                e.getNombre(),
                e.getAnio(),
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

    public Long getModeloId() {
        return modeloId;
    }

    public void setModeloId(Long modeloId) {
        this.modeloId = modeloId;
    }

    public String getModeloNombre() {
        return modeloNombre;
    }

    public void setModeloNombre(String modeloNombre) {
        this.modeloNombre = modeloNombre;
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

    public Integer getAnio() {
        return anio;
    }

    public void setAnio(Integer anio) {
        this.anio = anio;
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
