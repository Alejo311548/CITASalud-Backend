package com.eps.citas.dto;

import java.time.LocalDateTime;

public class ModificarCitaDto {
    private LocalDateTime nuevaFechaHora;

    public LocalDateTime getNuevaFechaHora() {
        return nuevaFechaHora;
    }

    public void setNuevaFechaHora(LocalDateTime nuevaFechaHora) {
        this.nuevaFechaHora = nuevaFechaHora;
    }
}
