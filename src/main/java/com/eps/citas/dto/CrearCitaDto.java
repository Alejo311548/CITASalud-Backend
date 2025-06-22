package com.eps.citas.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

@Schema(description = "Datos necesarios para agendar una cita médica")
public class CrearCitaDto {

    @Schema(description = "ID del profesional con el que se agenda la cita", example = "5", required = true)
    private Long profesionalId;

    @Schema(description = "ID de la sede en la que se realizará la cita", example = "3", required = true)
    private Long sedeId;

    @Schema(description = "Fecha y hora exacta de la cita en formato ISO 8601", example = "2025-06-23T09:30:00", required = true)
    private LocalDateTime fechaHora;

    // Constructor vacío
    public CrearCitaDto() {
    }

    // Getters y setters
    public Long getProfesionalId() {
        return profesionalId;
    }

    public void setProfesionalId(Long profesionalId) {
        this.profesionalId = profesionalId;
    }

    public Long getSedeId() {
        return sedeId;
    }

    public void setSedeId(Long sedeId) {
        this.sedeId = sedeId;
    }

    public LocalDateTime getFechaHora() {
        return fechaHora;
    }

    public void setFechaHora(LocalDateTime fechaHora) {
        this.fechaHora = fechaHora;
    }
}
