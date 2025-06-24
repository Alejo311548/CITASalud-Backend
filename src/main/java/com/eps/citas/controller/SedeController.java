package com.eps.citas.controller;

import com.eps.citas.model.Sede;
import com.eps.citas.repository.SedeRepository;

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

@Tag(name = "Sedes", description = "Operaciones para gestionar las sedes médicas disponibles")
@RestController
@RequestMapping("/api/sedes")
public class SedeController {

    @Autowired
    private SedeRepository repository;

    @Operation(summary = "Crear nueva sede", description = "Registra una nueva sede médica en el sistema.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Sede creada exitosamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Sede.class))),
            @ApiResponse(responseCode = "400", description = "Datos inválidos")
    })
    @PostMapping
    public ResponseEntity<Sede> crear(@RequestBody Sede sede) {
        Sede creada = repository.save(sede);
        return ResponseEntity.ok(creada);
    }

    @Operation(summary = "Obtener todas las sedes", description = "Devuelve la lista de todas las sedes registradas.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Listado de sedes obtenido correctamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Sede.class)))
    })
    @GetMapping
    public ResponseEntity<List<Sede>> obtenerTodas() {
        List<Sede> sedes = repository.findAll();
        return ResponseEntity.ok(sedes);
    }
}
