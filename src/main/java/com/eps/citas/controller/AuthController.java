package com.eps.citas.controller;

import com.eps.citas.dto.JwtResponseDto;
import com.eps.citas.dto.LoginDto;
import com.eps.citas.dto.RegistroUsuarioDto;
import com.eps.citas.model.Usuario;
import com.eps.citas.repository.UsuarioRepository;
import com.eps.citas.auth.util.JwtTokenUtil;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
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
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

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
            @ApiResponse(responseCode = "200", description = "Autenticación exitosa, devuelve el token JWT"),
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

            EntityModel<JwtResponseDto> resource = EntityModel.of(
                    new JwtResponseDto(token),
                    linkTo(methodOn(AuthController.class).login(loginRequest)).withSelfRel(),
                    linkTo(methodOn(AuthController.class).register(null)).withRel("register"),
                    linkTo(methodOn(AuthController.class).registerProfesional(null)).withRel("registerProfesional")
            );

            return ResponseEntity.ok(resource);
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Credenciales inválidas");
        }
    }

    @Operation(summary = "Registrar paciente", description = "Registra un nuevo usuario paciente en el sistema.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuario registrado exitosamente"),
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

        EntityModel<String> response = EntityModel.of(
                "Registro exitoso.",
                linkTo(methodOn(AuthController.class).register(dto)).withSelfRel(),
                linkTo(methodOn(AuthController.class).login(new LoginDto(dto.getEmail(), dto.getPassword()))).withRel("login")
        );

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Registrar profesional", description = "Registra un nuevo usuario con rol profesional.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Profesional registrado exitosamente"),
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

        EntityModel<String> response = EntityModel.of(
                "Profesional registrado exitosamente.",
                linkTo(methodOn(AuthController.class).registerProfesional(dto)).withSelfRel(),
                linkTo(methodOn(AuthController.class).login(new LoginDto(dto.getEmail(), dto.getPassword()))).withRel("login")
        );

        return ResponseEntity.ok(response);
    }
}
