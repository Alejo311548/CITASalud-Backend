package com.eps.citas.controller;

import com.eps.citas.model.Especialidad;
import com.eps.citas.repository.EspecialidadRepository;

import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Especialidades", description = "Operaciones para crear y consultar especialidades médicas")
@RestController
@RequestMapping("/api/especialidades")
public class EspecialidadController {

    @Autowired
    private EspecialidadRepository repository;

    @Operation(summary = "Crear especialidad", description = "Crea una nueva especialidad médica.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Especialidad creada exitosamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Especialidad.class))),
            @ApiResponse(responseCode = "400", description = "Datos inválidos")
    })
    @PostMapping
    public ResponseEntity<Especialidad> crear(@RequestBody Especialidad especialidad) {
        Especialidad creada = repository.save(especialidad);
        return ResponseEntity.ok(creada);
    }

    @Operation(summary = "Listar especialidades", description = "Obtiene todas las especialidades médicas registradas.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de especialidades obtenida correctamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Especialidad.class)))
    })
    @GetMapping
    public ResponseEntity<List<Especialidad>> obtenerTodas() {
        List<Especialidad> especialidades = repository.findAll();
        return ResponseEntity.ok(especialidades);
    }
}
