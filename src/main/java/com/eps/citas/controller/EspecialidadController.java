package com.eps.citas.controller;

import com.eps.citas.model.Especialidad;
import com.eps.citas.repository.EspecialidadRepository;

import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Tag(name = "Especialidades", description = "Operaciones para crear y consultar especialidades médicas")
@RestController
@RequestMapping("/api/especialidades")
public class EspecialidadController {

    @Autowired
    private EspecialidadRepository repository;

    @Operation(summary = "Crear especialidad", description = "Crea una nueva especialidad médica.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Especialidad creada exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos")
    })
    @PostMapping
    public ResponseEntity<EntityModel<Especialidad>> crear(@RequestBody Especialidad especialidad) {
        Especialidad creada = repository.save(especialidad);
        EntityModel<Especialidad> model = EntityModel.of(creada,
                linkTo(methodOn(EspecialidadController.class).crear(especialidad)).withSelfRel(),
                linkTo(methodOn(EspecialidadController.class).obtenerTodas()).withRel("todas"));

        return ResponseEntity.ok(model);
    }

    @Operation(summary = "Listar especialidades", description = "Obtiene todas las especialidades médicas registradas.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de especialidades obtenida correctamente")
    })
    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<Especialidad>>> obtenerTodas() {
        List<EntityModel<Especialidad>> especialidades = repository.findAll()
                .stream()
                .map(especialidad -> EntityModel.of(especialidad,
                        linkTo(methodOn(EspecialidadController.class).crear(especialidad)).withRel("crear")
                )).collect(Collectors.toList());

        return ResponseEntity.ok(CollectionModel.of(especialidades,
                linkTo(methodOn(EspecialidadController.class).obtenerTodas()).withSelfRel()));
    }
}
