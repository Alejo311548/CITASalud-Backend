package com.eps.citas.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;

@Entity
@Table(name = "profesionales")
@Schema(description = "Representa a un profesional de la salud que atiende citas.")
public class Profesional {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Identificador único del profesional", example = "101")
    private Long profesionalId;

    @Schema(description = "Nombre completo del profesional", example = "Dra. Camila Ríos")
    private String nombre;

    @ManyToOne
    @JoinColumn(name = "especialidad_id", nullable = false)
    @Schema(description = "Especialidad médica del profesional")
    private Especialidad especialidad;

    @ManyToOne
    @JoinColumn(name = "sede_id", nullable = false)
    @Schema(description = "Sede en la que trabaja el profesional")
    private Sede sede;

    // Constructor vacío
    public Profesional() {
    }

    // Constructor con todos los campos
    public Profesional(Long profesionalId, String nombre, Especialidad especialidad, Sede sede) {
        this.profesionalId = profesionalId;
        this.nombre = nombre;
        this.especialidad = especialidad;
        this.sede = sede;
    }

    // Constructor solo con ID
    public Profesional(Long profesionalId) {
        this.profesionalId = profesionalId;
    }

    // Getters y setters

    public Long getProfesionalId() {
        return profesionalId;
    }

    public void setProfesionalId(Long profesionalId) {
        this.profesionalId = profesionalId;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Especialidad getEspecialidad() {
        return especialidad;
    }

    public void setEspecialidad(Especialidad especialidad) {
        this.especialidad = especialidad;
    }

    public Sede getSede() {
        return sede;
    }

    public void setSede(Sede sede) {
        this.sede = sede;
    }
}
