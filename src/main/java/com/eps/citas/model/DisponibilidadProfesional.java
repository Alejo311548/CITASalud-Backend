package com.eps.citas.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;

import java.time.LocalTime;

@Entity
@Table(name = "disponibilidad_profesionales")
@Schema(description = "Representa la disponibilidad horaria semanal de un profesional de salud")
public class DisponibilidadProfesional {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Identificador único de la disponibilidad", example = "1")
    private Long disponibilidadId;

    @ManyToOne
    @JoinColumn(name = "profesional_id", nullable = false)
    @Schema(description = "Profesional asociado a esta franja de disponibilidad")
    private Profesional profesional;

    @Column(name = "dia_semana", nullable = false)
    @Schema(description = "Día de la semana en que aplica la disponibilidad (en inglés: MONDAY, TUESDAY...)", example = "MONDAY")
    private String diaSemana;

    @Column(name = "hora_inicio", nullable = false)
    @Schema(description = "Hora de inicio de atención para ese día", example = "08:00")
    private LocalTime horaInicio;

    @Column(name = "hora_fin", nullable = false)
    @Schema(description = "Hora de fin de atención para ese día", example = "16:00")
    private LocalTime horaFin;

    // Constructor vacío
    public DisponibilidadProfesional() {
    }

    // Constructor completo
    public DisponibilidadProfesional(Long disponibilidadId, Profesional profesional, String diaSemana, LocalTime horaInicio, LocalTime horaFin) {
        this.disponibilidadId = disponibilidadId;
        this.profesional = profesional;
        this.diaSemana = diaSemana;
        this.horaInicio = horaInicio;
        this.horaFin = horaFin;
    }

    // Getters y setters
    public Long getDisponibilidadId() {
        return disponibilidadId;
    }

    public void setDisponibilidadId(Long disponibilidadId) {
        this.disponibilidadId = disponibilidadId;
    }

    public Profesional getProfesional() {
        return profesional;
    }

    public void setProfesional(Profesional profesional) {
        this.profesional = profesional;
    }

    public String getDiaSemana() {
        return diaSemana;
    }

    public void setDiaSemana(String diaSemana) {
        this.diaSemana = diaSemana;
    }

    public LocalTime getHoraInicio() {
        return horaInicio;
    }

    public void setHoraInicio(LocalTime horaInicio) {
        this.horaInicio = horaInicio;
    }

    public LocalTime getHoraFin() {
        return horaFin;
    }

    public void setHoraFin(LocalTime horaFin) {
        this.horaFin = horaFin;
    }
}
