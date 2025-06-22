package com.eps.citas.controller;

import com.eps.citas.model.Sede;
import com.eps.citas.repository.SedeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Tag(name = "Sedes", description = "Operaciones para gestionar las sedes médicas disponibles")
@RestController
@RequestMapping("/api/sedes")
public class SedeController {

    @Autowired
    private SedeRepository repository;

    @Operation(summary = "Crear nueva sede", description = "Registra una nueva sede médica en el sistema.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Sede creada exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos")
    })
    @PostMapping
    public ResponseEntity<EntityModel<Sede>> crear(@RequestBody Sede sede) {
        Sede creada = repository.save(sede);

        EntityModel<Sede> model = EntityModel.of(creada,
                linkTo(methodOn(SedeController.class).crear(sede)).withSelfRel(),
                linkTo(methodOn(SedeController.class).obtenerTodas()).withRel("todas"));

        return ResponseEntity.ok(model);
    }

    @Operation(summary = "Obtener todas las sedes", description = "Devuelve la lista de todas las sedes registradas.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Listado de sedes obtenido correctamente")
    })
    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<Sede>>> obtenerTodas() {
        List<EntityModel<Sede>> sedes = repository.findAll().stream()
                .map(sede -> EntityModel.of(sede,
                        linkTo(methodOn(SedeController.class).crear(sede)).withRel("crear")))
                .collect(Collectors.toList());

        return ResponseEntity.ok(CollectionModel.of(sedes,
                linkTo(methodOn(SedeController.class).obtenerTodas()).withSelfRel()));
    }
}
