package com.eps.citas.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;

@Entity
@Table(name = "especialidades")
@Schema(description = "Entidad que representa una especialidad médica, como Pediatría, Dermatología, etc.")
public class Especialidad {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Identificador único de la especialidad", example = "1")
    private Long especialidadId;

    @Column(nullable = false)
    @Schema(description = "Nombre de la especialidad médica", example = "Pediatría")
    private String nombre;

    // Constructor vacío obligatorio para Jackson y JPA
    public Especialidad() {
    }

    // Constructor con todos los campos
    public Especialidad(Long especialidadId, String nombre) {
        this.especialidadId = especialidadId;
        this.nombre = nombre;
    }

    // Constructor solo con ID
    public Especialidad(Long especialidadId) {
        this.especialidadId = especialidadId;
    }

    // Getters y setters públicos

    public Long getEspecialidadId() {
        return especialidadId;
    }

    public void setEspecialidadId(Long especialidadId) {
        this.especialidadId = especialidadId;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}
