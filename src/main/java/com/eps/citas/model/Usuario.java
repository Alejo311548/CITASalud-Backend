package com.eps.citas.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;

@Entity
@Table(name = "usuarios")
@Schema(description = "Entidad que representa a un usuario del sistema, ya sea paciente o profesional.")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Identificador único del usuario", example = "1")
    private Long usuarioId;

    @Schema(description = "Nombre completo del usuario", example = "Juan Pérez")
    private String nombre;

    @Column(unique = true, nullable = false)
    @Schema(description = "Correo electrónico único del usuario", example = "juan.perez@example.com")
    private String email;

    @Schema(description = "Número de teléfono del usuario", example = "+57 3123456789")
    private String telefono;

    @Schema(description = "Rol del usuario en el sistema (ej. PACIENTE, PROFESIONAL, ADMIN)", example = "PACIENTE")
    private String rol;

    @Schema(description = "Hash de la contraseña del usuario. No se expone en respuestas.", accessMode = Schema.AccessMode.WRITE_ONLY)
    private String passwordHash;

    @Schema(description = "Estado del usuario (true = activo, false = inactivo)", example = "true")
    private Boolean estado = true;

    // Constructor sin argumentos
    public Usuario() {
    }

    // Constructor con todos los atributos
    public Usuario(Long usuarioId, String nombre, String email, String telefono, String rol, String passwordHash, Boolean estado) {
        this.usuarioId = usuarioId;
        this.nombre = nombre;
        this.email = email;
        this.telefono = telefono;
        this.rol = rol;
        this.passwordHash = passwordHash;
        this.estado = estado;
    }

    // Getters y Setters

    public Long getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(Long usuarioId) {
        this.usuarioId = usuarioId;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public Boolean getEstado() {
        return estado;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
    }

    @Override
    public String toString() {
        return "Usuario{" +
                "usuarioId=" + usuarioId +
                ", nombre='" + nombre + '\'' +
                ", email='" + email + '\'' +
                ", telefono='" + telefono + '\'' +
                ", rol='" + rol + '\'' +
                ", passwordHash='[PROTEGIDO]'" +
                ", estado=" + estado +
                '}';
    }
}
