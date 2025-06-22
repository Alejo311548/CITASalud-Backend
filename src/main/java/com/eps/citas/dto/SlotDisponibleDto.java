package com.eps.citas.dto;

import java.time.LocalDateTime;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Representa un intervalo de tiempo disponible para agendar una cita médica")
public class SlotDisponibleDto {

    @Schema(
            description = "Fecha y hora de inicio del intervalo disponible",
            example = "2025-06-23T09:00:00",
            required = true
    )
    private LocalDateTime inicio;

    @Schema(
            description = "Fecha y hora de fin del intervalo disponible",
            example = "2025-06-23T09:30:00",
            required = true
    )
    private LocalDateTime fin;

    // Constructor con todos los campos
    public SlotDisponibleDto(LocalDateTime inicio, LocalDateTime fin) {
        this.inicio = inicio;
        this.fin = fin;
    }

    // Getters y setters

    public LocalDateTime getInicio() {
        return inicio;
    }

    public void setInicio(LocalDateTime inicio) {
        this.inicio = inicio;
    }

    public LocalDateTime getFin() {
        return fin;
    }

    public void setFin(LocalDateTime fin) {
        this.fin = fin;
    }
}
