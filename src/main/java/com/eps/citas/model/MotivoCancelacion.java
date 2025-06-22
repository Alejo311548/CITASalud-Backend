package com.eps.citas.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;

@Entity
@Table(name = "motivos_cancelacion")
@Schema(description = "Motivo predefinido por el cual una cita puede ser cancelada.")
public class MotivoCancelacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Identificador único del motivo de cancelación", example = "1")
    private Long id;

    @Column(nullable = false)
    @Schema(description = "Descripción del motivo de cancelación", example = "El paciente no puede asistir")
    private String descripcion;

    // Getters y setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}
