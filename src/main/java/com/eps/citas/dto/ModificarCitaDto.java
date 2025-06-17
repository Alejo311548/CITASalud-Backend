package com.eps.citas.dto;

import java.time.LocalDateTime;

public class ModificarCitaDto {

    private Long profesionalId;
    private Long sedeId;
    private LocalDateTime nuevaFechaHora;

    // Constructor vacío obligatorio para que Spring deserialice correctamente
    public ModificarCitaDto() {
    }

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

    public LocalDateTime getNuevaFechaHora() {
        return nuevaFechaHora;
    }

    public void setNuevaFechaHora(LocalDateTime nuevaFechaHora) {
        this.nuevaFechaHora = nuevaFechaHora;
    }
}
