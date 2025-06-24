package com.eps.citas.controller;

import com.eps.citas.model.Especialidad;
import com.eps.citas.model.Profesional;
import com.eps.citas.model.Sede;
import com.eps.citas.repository.ProfesionalRepository;

import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Profesionales", description = "Operaciones relacionadas con profesionales de la salud")
@RestController
@RequestMapping("/api/profesionales")
public class ProfesionalController {

    private final ProfesionalRepository profesionalRepository;

    @Autowired
    public ProfesionalController(ProfesionalRepository profesionalRepository) {
        this.profesionalRepository = profesionalRepository;
    }

    @Operation(summary = "Listar todos los profesionales", description = "Obtiene todos los profesionales registrados.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Listado exitoso",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Profesional.class)))
    })
    @GetMapping
    public ResponseEntity<List<Profesional>> listarTodos() {
        List<Profesional> profesionales = profesionalRepository.findAll();
        return ResponseEntity.ok(profesionales);
    }

    @Operation(summary = "Filtrar profesionales por especialidad y sede",
            description = "Devuelve los profesionales según una especialidad y sede médica.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Filtrado exitoso",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Profesional.class))),
            @ApiResponse(responseCode = "400", description = "Parámetros inválidos")
    })
    @GetMapping("/filtrar")
    public ResponseEntity<List<Profesional>> filtrarPorEspecialidadYSede(
            @Parameter(description = "ID de la especialidad") @RequestParam Long especialidadId,
            @Parameter(description = "ID de la sede") @RequestParam Long sedeId) {

        List<Profesional> filtrados = profesionalRepository
                .findByEspecialidad_EspecialidadIdAndSede_SedeId(especialidadId, sedeId);

        return ResponseEntity.ok(filtrados);
    }

    @Operation(summary = "Crear nuevo profesional", description = "Registra un nuevo profesional con nombre, especialidad y sede.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Profesional creado correctamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Profesional.class))),
            @ApiResponse(responseCode = "400", description = "Datos inválidos")
    })
    @PostMapping
    public ResponseEntity<Profesional> crearProfesional(
            @Parameter(description = "Nombre del profesional") @RequestParam String nombre,
            @Parameter(description = "ID de la especialidad") @RequestParam Long especialidadId,
            @Parameter(description = "ID de la sede") @RequestParam Long sedeId) {

        Especialidad especialidad = new Especialidad(especialidadId);
        Sede sede = new Sede(sedeId);

        Profesional profesional = new Profesional();
        profesional.setNombre(nombre);
        profesional.setEspecialidad(especialidad);
        profesional.setSede(sede);

        Profesional guardado = profesionalRepository.save(profesional);

        return ResponseEntity.ok(guardado);
    }
}
