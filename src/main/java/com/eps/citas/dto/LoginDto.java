package com.eps.citas.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Credenciales necesarias para iniciar sesión")
public class LoginDto {

    @Schema(description = "Correo electrónico del usuario", example = "usuario@ejemplo.com", required = true)
    private String email;

    @Schema(description = "Contraseña del usuario", example = "miContraseñaSegura123", required = true)
    private String password;

    public LoginDto() {
    }

    public LoginDto(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "LoginDto{" +
                "email='" + email + '\'' +
                ", password='[PROTECTED]'}";
    }
}
