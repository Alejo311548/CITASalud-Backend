package com.eps.citas.controller;

import com.eps.citas.dto.CancelarCitaDto;
import com.eps.citas.dto.CrearCitaDto;
import com.eps.citas.dto.ModificarCitaDto;
import com.eps.citas.dto.SlotDisponibleDto;
import com.eps.citas.model.Cita;
import com.eps.citas.service.CitaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Tag(name = "Citas Médicas", description = "Gestión de citas: agendar, modificar, cancelar y consultar")
@RestController
@RequestMapping("/api/citas")
public class CitaController {

    private final CitaService citaService;

    public CitaController(CitaService citaService) {
        this.citaService = citaService;
    }

    @Operation(summary = "Consultar disponibilidad", description = "Obtiene los horarios disponibles de un profesional en una fecha específica.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de horarios disponibles"),
            @ApiResponse(responseCode = "400", description = "Fecha o profesional inválido")
    })
    @GetMapping("/disponibilidad/{profesionalId}/{fecha}")
    public ResponseEntity<List<SlotDisponibleDto>> obtenerDisponibilidad(
            @PathVariable Long profesionalId,
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha) {
        return ResponseEntity.ok(citaService.obtenerDisponibilidad(profesionalId, fecha));
    }

    @Operation(summary = "Agendar cita", description = "Permite a un usuario autenticado agendar una cita médica.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Cita agendada correctamente"),
            @ApiResponse(responseCode = "401", description = "Usuario no autenticado"),
            @ApiResponse(responseCode = "400", description = "Conflicto de horario o datos inválidos")
    })
    @PostMapping
    public ResponseEntity<EntityModel<String>> agendarCita(@RequestBody CrearCitaDto dto, Principal principal) {
        if (principal == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
        citaService.agendarCita(principal.getName(), dto);

        EntityModel<String> model = EntityModel.of("Cita agendada correctamente",
                linkTo(methodOn(CitaController.class).listarCitasUsuario(principal)).withRel("mis-citas"));
        return ResponseEntity.status(HttpStatus.CREATED).body(model);
    }

    @Operation(summary = "Listar citas del usuario", description = "Muestra todas las citas agendadas por el usuario autenticado.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Citas obtenidas correctamente"),
            @ApiResponse(responseCode = "401", description = "Usuario no autenticado")
    })
    @GetMapping("/mis-citas")
    public ResponseEntity<CollectionModel<EntityModel<Cita>>> listarCitasUsuario(Principal principal) {
        if (principal == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        List<Cita> citas = citaService.obtenerCitasPorUsuario(principal.getName());

        List<EntityModel<Cita>> citasModel = citas.stream().map(cita ->
                EntityModel.of(cita,
                        linkTo(methodOn(CitaController.class).obtenerCitaPorId(cita.getCitaId(), principal)).withSelfRel(),
                        linkTo(methodOn(CitaController.class).cancelarCita(cita.getCitaId(), new CancelarCitaDto(), principal)).withRel("cancelar"),
                        linkTo(methodOn(CitaController.class).modificarCita(cita.getCitaId(), new ModificarCitaDto(), principal)).withRel("modificar")
                )
        ).collect(Collectors.toList());

        return ResponseEntity.ok(CollectionModel.of(citasModel,
                linkTo(methodOn(CitaController.class).listarCitasUsuario(principal)).withSelfRel()));
    }

    @Operation(summary = "Cancelar cita", description = "Permite al usuario cancelar una cita agendada.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cita cancelada correctamente"),
            @ApiResponse(responseCode = "400", description = "Motivo inválido o error de validación"),
            @ApiResponse(responseCode = "401", description = "Usuario no autenticado")
    })
    @PutMapping("/{id}/cancelar")
    public ResponseEntity<EntityModel<String>> cancelarCita(
            @PathVariable Long id,
            @RequestBody CancelarCitaDto cancelarDto,
            Principal principal) {
        if (principal == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        try {
            citaService.cancelarCita(id, principal.getName(), cancelarDto);
            EntityModel<String> model = EntityModel.of("Cita cancelada correctamente",
                    linkTo(methodOn(CitaController.class).listarCitasUsuario(principal)).withRel("mis-citas"));
            return ResponseEntity.ok(model);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(EntityModel.of(e.getMessage()));
        }
    }

    @Operation(summary = "Modificar cita", description = "Permite al usuario modificar la fecha y hora de una cita.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cita modificada correctamente"),
            @ApiResponse(responseCode = "400", description = "Fecha inválida o cita cancelada"),
            @ApiResponse(responseCode = "401", description = "Usuario no autenticado")
    })
    @PutMapping("/modificar/{id}")
    public ResponseEntity<EntityModel<String>> modificarCita(
            @PathVariable Long id,
            @RequestBody ModificarCitaDto dto,
            Principal principal) {
        if (principal == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        try {
            citaService.modificarCita(id, principal.getName(), dto);
            EntityModel<String> model = EntityModel.of("Cita modificada correctamente",
                    linkTo(methodOn(CitaController.class).obtenerCitaPorId(id, principal)).withRel("ver"));
            return ResponseEntity.ok(model);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(EntityModel.of(e.getMessage()));
        }
    }

    @Operation(summary = "Consultar cita por ID", description = "Obtiene una cita específica si pertenece al usuario autenticado.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cita obtenida correctamente"),
            @ApiResponse(responseCode = "404", description = "Cita no encontrada"),
            @ApiResponse(responseCode = "401", description = "Usuario no autenticado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<Cita>> obtenerCitaPorId(@PathVariable Long id, Principal principal) {
        if (principal == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        Cita cita = citaService.obtenerCitaPorIdYUsuario(id, principal.getName());
        if (cita == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        EntityModel<Cita> model = EntityModel.of(cita,
                linkTo(methodOn(CitaController.class).listarCitasUsuario(principal)).withRel("mis-citas"),
                linkTo(methodOn(CitaController.class).modificarCita(id, new ModificarCitaDto(), principal)).withRel("modificar"),
                linkTo(methodOn(CitaController.class).cancelarCita(id, new CancelarCitaDto(), principal)).withRel("cancelar")
        );

        return ResponseEntity.ok(model);
    }
}
