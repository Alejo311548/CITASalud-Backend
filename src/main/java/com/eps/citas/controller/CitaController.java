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
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDate;
import java.util.List;

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
            @ApiResponse(responseCode = "200", description = "Lista de horarios disponibles",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = SlotDisponibleDto.class))),
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
            @ApiResponse(responseCode = "201", description = "Cita agendada correctamente",
                    content = @Content(mediaType = "text/plain")),
            @ApiResponse(responseCode = "401", description = "Usuario no autenticado"),
            @ApiResponse(responseCode = "400", description = "Conflicto de horario o datos inválidos")
    })
    @PostMapping
    public ResponseEntity<String> agendarCita(@RequestBody CrearCitaDto dto, Principal principal) {
        if (principal == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Usuario no autenticado");
        }

        citaService.agendarCita(principal.getName(), dto);
        return ResponseEntity.status(HttpStatus.CREATED).body("Cita agendada correctamente");
    }

    @Operation(summary = "Listar citas del usuario", description = "Muestra todas las citas agendadas por el usuario autenticado.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Citas obtenidas correctamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Cita.class))),
            @ApiResponse(responseCode = "401", description = "Usuario no autenticado")
    })
    @GetMapping("/mis-citas")
    public ResponseEntity<List<Cita>> listarCitasUsuario(Principal principal) {
        if (principal == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        List<Cita> citas = citaService.obtenerCitasPorUsuario(principal.getName());
        return ResponseEntity.ok(citas);
    }

    @Operation(summary = "Cancelar cita", description = "Permite al usuario cancelar una cita agendada.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cita cancelada correctamente",
                    content = @Content(mediaType = "text/plain")),
            @ApiResponse(responseCode = "400", description = "Motivo inválido o error de validación"),
            @ApiResponse(responseCode = "401", description = "Usuario no autenticado")
    })
    @PutMapping("/{id}/cancelar")
    public ResponseEntity<String> cancelarCita(
            @PathVariable Long id,
            @RequestBody CancelarCitaDto cancelarDto,
            Principal principal) {
        if (principal == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Usuario no autenticado");
        }

        try {
            citaService.cancelarCita(id, principal.getName(), cancelarDto);
            return ResponseEntity.ok("Cita cancelada correctamente");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @Operation(summary = "Modificar cita", description = "Permite al usuario modificar la fecha y hora de una cita.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cita modificada correctamente",
                    content = @Content(mediaType = "text/plain")),
            @ApiResponse(responseCode = "400", description = "Fecha inválida o cita cancelada"),
            @ApiResponse(responseCode = "401", description = "Usuario no autenticado")
    })
    @PutMapping("/modificar/{id}")
    public ResponseEntity<String> modificarCita(
            @PathVariable Long id,
            @RequestBody ModificarCitaDto dto,
            Principal principal) {
        if (principal == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Usuario no autenticado");
        }

        try {
            citaService.modificarCita(id, principal.getName(), dto);
            return ResponseEntity.ok("Cita modificada correctamente");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @Operation(summary = "Consultar cita por ID", description = "Obtiene una cita específica si pertenece al usuario autenticado.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cita obtenida correctamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Cita.class))),
            @ApiResponse(responseCode = "404", description = "Cita no encontrada"),
            @ApiResponse(responseCode = "401", description = "Usuario no autenticado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Cita> obtenerCitaPorId(@PathVariable Long id, Principal principal) {
        if (principal == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        Cita cita = citaService.obtenerCitaPorIdYUsuario(id, principal.getName());
        if (cita == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        return ResponseEntity.ok(cita);
    }
}
