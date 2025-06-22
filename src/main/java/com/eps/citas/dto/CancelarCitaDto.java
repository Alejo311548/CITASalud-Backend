package com.eps.citas.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Datos para cancelar una cita médica")
public class CancelarCitaDto {

    @Schema(description = "Motivo escrito por el usuario para la cancelación", example = "No puedo asistir por motivos personales")
    private String motivoCancelacion;

    @Schema(description = "ID del motivo predefinido en la base de datos", example = "2")
    private Long motivoId;

    // Getters y setters
    public String getMotivoCancelacion() {
        return motivoCancelacion;
    }
    public void setMotivoCancelacion(String motivoCancelacion) {
        this.motivoCancelacion = motivoCancelacion;
    }

    public Long getMotivoId() {
        return motivoId;
    }
    public void setMotivoId(Long motivoId) {
        this.motivoId = motivoId;
    }
}
