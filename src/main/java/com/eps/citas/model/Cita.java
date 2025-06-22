package com.eps.citas.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "citas")
@Schema(description = "Entidad que representa una cita médica en el sistema")
public class Cita {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Identificador único de la cita", example = "1")
    private Long citaId;

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    @Schema(description = "Usuario que agendó la cita")
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "profesional_id", nullable = false)
    @Schema(description = "Profesional médico asignado a la cita")
    private Profesional profesional;

    @ManyToOne
    @JoinColumn(name = "sede_id", nullable = false)
    @Schema(description = "Sede médica donde se realizará la cita")
    private Sede sede;

    @Column(name = "fecha_hora", nullable = false)
    @Schema(description = "Fecha y hora programadas para la cita", example = "2025-06-30T14:30:00")
    private LocalDateTime fechaHora;

    @Column(nullable = false)
    @Schema(description = "Estado actual de la cita (e.g., AGENDADA, CANCELADA)", example = "AGENDADA")
    private String estado;

    @Column(name = "motivo_cancelacion")
    @Schema(description = "Motivo textual proporcionado por el usuario para cancelar la cita (si aplica)", example = "Cambio de planes")
    private String motivoCancelacion;

    @ManyToOne
    @JoinColumn(name = "motivo_id")
    @Schema(description = "Motivo de cancelación predefinido (si aplica)")
    private MotivoCancelacion motivo;

    // Constructor vacío
    public Cita() {}

    // Constructor con todos los campos
    public Cita(Long citaId, Usuario usuario, Profesional profesional, Sede sede,
                LocalDateTime fechaHora, String estado, String motivoCancelacion,
                MotivoCancelacion motivo) {
        this.citaId = citaId;
        this.usuario = usuario;
        this.profesional = profesional;
        this.sede = sede;
        this.fechaHora = fechaHora;
        this.estado = estado;
        this.motivoCancelacion = motivoCancelacion;
        this.motivo = motivo;
    }

    // Getters y setters

    public Long getCitaId() {
        return citaId;
    }

    public void setCitaId(Long citaId) {
        this.citaId = citaId;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Profesional getProfesional() {
        return profesional;
    }

    public void setProfesional(Profesional profesional) {
        this.profesional = profesional;
    }

    public Sede getSede() {
        return sede;
    }

    public void setSede(Sede sede) {
        this.sede = sede;
    }

    public LocalDateTime getFechaHora() {
        return fechaHora;
    }

    public void setFechaHora(LocalDateTime fechaHora) {
        this.fechaHora = fechaHora;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getMotivoCancelacion() {
        return motivoCancelacion;
    }

    public void setMotivoCancelacion(String motivoCancelacion) {
        this.motivoCancelacion = motivoCancelacion;
    }

    public MotivoCancelacion getMotivo() {
        return motivo;
    }

    public void setMotivo(MotivoCancelacion motivo) {
        this.motivo = motivo;
    }
}
