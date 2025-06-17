package com.eps.citas.controller;

import com.eps.citas.dto.CancelarCitaDto;
import com.eps.citas.dto.CrearCitaDto;
import com.eps.citas.dto.ModificarCitaDto;
import com.eps.citas.dto.SlotDisponibleDto;
import com.eps.citas.model.Cita;
import com.eps.citas.service.CitaService;
import com.eps.citas.dto.ModificarCitaDto;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDate;
import java.util.List;


@RestController
@RequestMapping("/api/citas")
public class CitaController {

    private final CitaService citaService;

    // Constructor manual (reemplaza @RequiredArgsConstructor)
    public CitaController(CitaService citaService) {
        this.citaService = citaService;
    }

    @GetMapping("/disponibilidad/{profesionalId}/{fecha}")
    public ResponseEntity<List<SlotDisponibleDto>> obtenerDisponibilidad(
            @PathVariable Long profesionalId,
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha) {

        return ResponseEntity.ok(citaService.obtenerDisponibilidad(profesionalId, fecha));
    }

    @PostMapping
    public ResponseEntity<?> agendarCita(@RequestBody CrearCitaDto dto, Principal principal) {
        if (principal == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Usuario no autenticado");
        }
        citaService.agendarCita(principal.getName(), dto);
        return ResponseEntity.status(HttpStatus.CREATED).body("Cita agendada correctamente");
    }

    // Nuevo endpoint para listar citas del usuario autenticado
    @GetMapping("/mis-citas")
    public ResponseEntity<?> listarCitasUsuario(Principal principal) {
        if (principal == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Usuario no autenticado");
        }
        List<Cita> citas = citaService.obtenerCitasPorUsuario(principal.getName());
        return ResponseEntity.ok(citas);
    }

    @PutMapping("/{id}/cancelar")
    public ResponseEntity<?> cancelarCita(
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
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al cancelar la cita");
        }
    }

    @PutMapping("/modificar/{id}")
    public ResponseEntity<?> modificarCita(
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
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al modificar la cita");
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerCitaPorId(@PathVariable Long id, Principal principal) {
        if (principal == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Usuario no autenticado");
        }
        try {
            Cita cita = citaService.obtenerCitaPorIdYUsuario(id, principal.getName());
            if (cita == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Cita no encontrada");
            }
            return ResponseEntity.ok(cita);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al obtener la cita");
        }
    }



}
