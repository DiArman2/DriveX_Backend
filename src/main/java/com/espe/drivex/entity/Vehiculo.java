package com.espe.drivex.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "vehiculos")
public class Vehiculo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "version_id", nullable = false)
    private Version version;

    @ManyToOne
    @JoinColumn(name = "propietario_id", nullable = false)
    private Usuario propietario;

    @Column(nullable = false, unique = true)
    private String placa;

    private String color;

    @Column(nullable = false)
    private String transmision;

    @Column(nullable = false)
    private String tipoCombustible;

    @Column(nullable = false)
    private Integer asientos;

    @Column(nullable = false)
    private BigDecimal precioDia;

    private String descripcion;

    private String ubicacion;

    private Boolean disponible = true;

    private Boolean activo = true;

    private LocalDateTime fechaCreacion;

    private LocalDateTime fechaActualizacion;

    public Vehiculo() {
    }

    public Vehiculo(Long id, Version version, Usuario propietario, String placa, String color,
                     String transmision, String tipoCombustible, Integer asientos, BigDecimal precioDia,
                     String descripcion, String ubicacion, Boolean disponible, Boolean activo,
                     LocalDateTime fechaCreacion, LocalDateTime fechaActualizacion) {
        this.id = id;
        this.version = version;
        this.propietario = propietario;
        this.placa = placa;
        this.color = color;
        this.transmision = transmision;
        this.tipoCombustible = tipoCombustible;
        this.asientos = asientos;
        this.precioDia = precioDia;
        this.descripcion = descripcion;
        this.ubicacion = ubicacion;
        this.disponible = disponible;
        this.activo = activo;
        this.fechaCreacion = fechaCreacion;
        this.fechaActualizacion = fechaActualizacion;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Version getVersion() {
        return version;
    }

    public void setVersion(Version version) {
        this.version = version;
    }

    public Usuario getPropietario() {
        return propietario;
    }

    public void setPropietario(Usuario propietario) {
        this.propietario = propietario;
    }

    public String getPlaca() {
        return placa;
    }

    public void setPlaca(String placa) {
        this.placa = placa;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getTransmision() {
        return transmision;
    }

    public void setTransmision(String transmision) {
        this.transmision = transmision;
    }

    public String getTipoCombustible() {
        return tipoCombustible;
    }

    public void setTipoCombustible(String tipoCombustible) {
        this.tipoCombustible = tipoCombustible;
    }

    public Integer getAsientos() {
        return asientos;
    }

    public void setAsientos(Integer asientos) {
        this.asientos = asientos;
    }

    public BigDecimal getPrecioDia() {
        return precioDia;
    }

    public void setPrecioDia(BigDecimal precioDia) {
        this.precioDia = precioDia;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }

    public Boolean getDisponible() {
        return disponible;
    }

    public void setDisponible(Boolean disponible) {
        this.disponible = disponible;
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

    public LocalDateTime getFechaActualizacion() {
        return fechaActualizacion;
    }

    public void setFechaActualizacion(LocalDateTime fechaActualizacion) {
        this.fechaActualizacion = fechaActualizacion;
    }
}
