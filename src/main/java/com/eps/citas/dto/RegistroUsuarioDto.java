package com.eps.citas.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "DTO utilizado para registrar un nuevo usuario, ya sea paciente o profesional")
public class RegistroUsuarioDto {

    @Schema(
            description = "Nombre completo del usuario",
            example = "María López",
            required = true
    )
    @NotBlank(message = "El nombre es obligatorio")
    @Size(min = 2, max = 100, message = "El nombre debe tener entre 2 y 100 caracteres")
    private String nombre;

    @Schema(
            description = "Correo electrónico único del usuario",
            example = "maria.lopez@example.com",
            required = true
    )
    @NotBlank(message = "El email es obligatorio")
    @Email(message = "El email no es válido")
    private String email;

    @Schema(
            description = "Número de teléfono del usuario",
            example = "3001234567",
            required = true
    )
    @NotBlank(message = "El teléfono es obligatorio")
    @Size(min = 10, max = 15, message = "El teléfono debe tener entre 10 y 15 caracteres")
    private String telefono;

    @Schema(
            description = "Contraseña segura para la cuenta del usuario",
            example = "MiClaveSegura123",
            required = true
    )
    @NotBlank(message = "La contraseña es obligatoria")
    @Size(min = 6, message = "La contraseña debe tener al menos 6 caracteres")
    private String password;

    @Schema(
            description = "Rol del usuario. Por defecto es 'PACIENTE'. Este campo puede ser ignorado en la creación de pacientes.",
            example = "PACIENTE",
            required = false
    )
    private String rol = "PACIENTE";

    // Constructor sin parámetros
    public RegistroUsuarioDto() {
    }

    // Constructor con todos los atributos
    public RegistroUsuarioDto(String nombre, String email, String telefono, String password, String rol) {
        this.nombre = nombre;
        this.email = email;
        this.telefono = telefono;
        this.password = password;
        this.rol = rol;
    }

    // Getters y Setters

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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    // Método toString (opcional)
    @Override
    public String toString() {
        return "RegistroUsuarioDto{" +
                "nombre='" + nombre + '\'' +
                ", email='" + email + '\'' +
                ", telefono='" + telefono + '\'' +
                ", password='[PROTEGIDO]'" +
                ", rol='" + rol + '\'' +
                '}';
    }
}
