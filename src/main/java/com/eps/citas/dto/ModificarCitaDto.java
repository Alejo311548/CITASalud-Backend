package com.eps.citas.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;

@Schema(description = "DTO para modificar una cita médica ya existente")
public class ModificarCitaDto {

    @Schema(
            description = "Nueva fecha y hora de la cita",
            example = "2025-07-01T15:30:00",
            required = true
    )
    private LocalDateTime nuevaFechaHora;

    public LocalDateTime getNuevaFechaHora() {
        return nuevaFechaHora;
    }

    public void setNuevaFechaHora(LocalDateTime nuevaFechaHora) {
        this.nuevaFechaHora = nuevaFechaHora;
    }
}
