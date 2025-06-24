package com.eps.citas.controller;

import com.eps.citas.dto.JwtResponseDto;
import com.eps.citas.dto.LoginDto;
import com.eps.citas.dto.RegistroUsuarioDto;
import com.eps.citas.model.Usuario;
import com.eps.citas.repository.UsuarioRepository;
import com.eps.citas.auth.util.JwtTokenUtil;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Autenticación", description = "Operaciones de login y registro de usuarios")
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenUtil jwtUtil;

    @Autowired
    private UserDetailsService userDetailsService;

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AuthController(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Operation(summary = "Autenticar usuario", description = "Genera un token JWT a partir del email y la contraseña.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Autenticación exitosa, devuelve el token JWT",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = JwtResponseDto.class))),
            @ApiResponse(responseCode = "401", description = "Credenciales inválidas")
    })
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDto loginRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getEmail(),
                            loginRequest.getPassword()
                    )
            );
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String token = jwtUtil.generateToken(userDetails.getUsername());

            return ResponseEntity.ok(new JwtResponseDto(token));
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Credenciales inválidas");
        }
    }

    @Operation(summary = "Registrar paciente", description = "Registra un nuevo usuario paciente en el sistema.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuario registrado exitosamente",
                    content = @Content(mediaType = "text/plain")),
            @ApiResponse(responseCode = "400", description = "Email ya registrado")
    })
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody @Valid RegistroUsuarioDto dto) {
        if (usuarioRepository.findByEmail(dto.getEmail()).isPresent()) {
            return ResponseEntity.badRequest().body("Email ya registrado.");
        }

        Usuario usuario = new Usuario();
        usuario.setNombre(dto.getNombre());
        usuario.setEmail(dto.getEmail());
        usuario.setTelefono(dto.getTelefono());
        usuario.setRol("PACIENTE");
        usuario.setPasswordHash(passwordEncoder.encode(dto.getPassword()));
        usuarioRepository.save(usuario);

        return ResponseEntity.ok("Registro exitoso.");
    }

    @Operation(summary = "Registrar profesional", description = "Registra un nuevo usuario con rol profesional.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Profesional registrado exitosamente",
                    content = @Content(mediaType = "text/plain")),
            @ApiResponse(responseCode = "400", description = "Email ya registrado")
    })
    @PostMapping("/register/profesional")
    public ResponseEntity<?> registerProfesional(@RequestBody @Valid RegistroUsuarioDto dto) {
        if (usuarioRepository.findByEmail(dto.getEmail()).isPresent()) {
            return ResponseEntity.badRequest().body("Email ya registrado.");
        }

        Usuario profesional = new Usuario();
        profesional.setNombre(dto.getNombre());
        profesional.setEmail(dto.getEmail());
        profesional.setTelefono(dto.getTelefono());
        profesional.setRol("PROFESIONAL");
        profesional.setPasswordHash(passwordEncoder.encode(dto.getPassword()));
        usuarioRepository.save(profesional);

        return ResponseEntity.ok("Profesional registrado exitosamente.");
    }
}
