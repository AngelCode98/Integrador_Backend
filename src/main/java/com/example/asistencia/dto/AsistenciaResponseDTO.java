package com.example.asistencia.dto;

import java.time.LocalDate;

public class AsistenciaResponseDTO {

    private Long id;
    private LocalDate fecha;
    private String estado;
    private String observacion;
    private Boolean justificada;

    // Información del estudiante
    private Long estudianteId;
    private String estudianteNombre;
    private String estudianteIdentificacion;

    // Información del grupo
    private Long grupoId;
    private String grupoNombre;
    private String grupoMateria;

    // ------------------------------------------------
    // Getters y Setters
    // ------------------------------------------------

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public Boolean getJustificada() {
        return justificada;
    }

    public void setJustificada(Boolean justificada) {
        this.justificada = justificada;
    }

    public Long getEstudianteId() {
        return estudianteId;
    }

    public void setEstudianteId(Long estudianteId) {
        this.estudianteId = estudianteId;
    }

    public String getEstudianteNombre() {
        return estudianteNombre;
    }

    public void setEstudianteNombre(String estudianteNombre) {
        this.estudianteNombre = estudianteNombre;
    }

    public String getEstudianteIdentificacion() {
        return estudianteIdentificacion;
    }

    public void setEstudianteIdentificacion(String estudianteIdentificacion) {
        this.estudianteIdentificacion = estudianteIdentificacion;
    }

    public Long getGrupoId() {
        return grupoId;
    }

    public void setGrupoId(Long grupoId) {
        this.grupoId = grupoId;
    }

    public String getGrupoNombre() {
        return grupoNombre;
    }

    public void setGrupoNombre(String grupoNombre) {
        this.grupoNombre = grupoNombre;
    }

    public String getGrupoMateria() {
        return grupoMateria;
    }

    public void setGrupoMateria(String grupoMateria) {
        this.grupoMateria = grupoMateria;
    }
}
