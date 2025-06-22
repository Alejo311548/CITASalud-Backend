package com.eps.citas.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Respuesta enviada tras una autenticación exitosa. Contiene el JWT para futuras solicitudes.")
public class JwtResponseDto {

    @Schema(description = "Token JWT generado para autenticación", example = "eyJhbGciOiJIUzI1NiJ9...")
    private String token;

    public JwtResponseDto(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }
}
