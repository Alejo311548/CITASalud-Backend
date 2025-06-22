package com.eps.citas.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;

@Entity
@Table(name = "sedes")
@Schema(description = "Representa una sede o ubicación física donde se prestan los servicios médicos.")
public class Sede {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Identificador único de la sede", example = "1")
    private Long sedeId;

    @Schema(description = "Nombre de la sede", example = "Centro Médico Norte")
    private String nombre;

    @Schema(description = "Dirección de la sede", example = "Calle 123 #45-67, Bogotá")
    private String direccion;

    @Schema(description = "Teléfono de contacto de la sede", example = "+57 301 2345678")
    private String telefono;

    // Constructor vacío
    public Sede() {
    }

    // Constructor con todos los campos
    public Sede(Long sedeId, String nombre, String direccion, String telefono) {
        this.sedeId = sedeId;
        this.nombre = nombre;
        this.direccion = direccion;
        this.telefono = telefono;
    }

    // Constructor solo con ID
    public Sede(Long sedeId) {
        this.sedeId = sedeId;
    }

    // Getters y setters

    public Long getSedeId() {
        return sedeId;
    }

    public void setSedeId(Long sedeId) {
        this.sedeId = sedeId;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }
}
