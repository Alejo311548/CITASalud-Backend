package com.eps.citas.service;

import com.eps.citas.dto.CancelarCitaDto;
import com.eps.citas.dto.CrearCitaDto;
import com.eps.citas.dto.SlotDisponibleDto;
import com.eps.citas.model.*;
import com.eps.citas.repository.*;
import com.eps.citas.dto.ModificarCitaDto;


import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;

@Service
public class CitaService {

    private final CitaRepository citaRepository;
    private final ProfesionalRepository profesionalRepository;
    private final DisponibilidadRepository disponibilidadRepository;
    private final UsuarioRepository usuarioRepository;
    private final SedeRepository sedeRepository;

    @Autowired
    private EmailService emailService;

    private final MotivoCancelacionRepository motivoCancelacionRepository;

    public CitaService(CitaRepository citaRepository,
                       ProfesionalRepository profesionalRepository,
                       DisponibilidadRepository disponibilidadRepository,
                       UsuarioRepository usuarioRepository,
                       SedeRepository sedeRepository,
                       MotivoCancelacionRepository motivoCancelacionRepository) {
        this.citaRepository = citaRepository;
        this.profesionalRepository = profesionalRepository;
        this.disponibilidadRepository = disponibilidadRepository;
        this.usuarioRepository = usuarioRepository;
        this.sedeRepository = sedeRepository;
        this.motivoCancelacionRepository = motivoCancelacionRepository;
    }

    public List<SlotDisponibleDto> obtenerDisponibilidad(Long profesionalId, LocalDate fecha) {
        List<DisponibilidadProfesional> disponibilidad =
                disponibilidadRepository.findByProfesional_ProfesionalId(profesionalId);

        DayOfWeek dia = fecha.getDayOfWeek();

        return disponibilidad.stream()
                .filter(d -> d.getDiaSemana().equalsIgnoreCase(dia.name()))
                .flatMap(d -> {
                    List<SlotDisponibleDto> slots = new ArrayList<>();
                    LocalTime start = d.getHoraInicio();
                    LocalTime end = d.getHoraFin();

                    while (start.plusMinutes(30).isBefore(end) || start.plusMinutes(30).equals(end)) {
                        LocalDateTime slotInicio = LocalDateTime.of(fecha, start);
                        if (!citaRepository.existsByProfesional_ProfesionalIdAndFechaHora(profesionalId, slotInicio)) {
                            slots.add(new SlotDisponibleDto(slotInicio, slotInicio.plusMinutes(30)));
                        }
                        start = start.plusMinutes(30);
                    }
                    return slots.stream();
                }).toList();
    }

    public void agendarCita(String emailUsuario, CrearCitaDto dto) {
        Usuario usuario = usuarioRepository.findByEmail(emailUsuario)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        if (citaRepository.existsByProfesional_ProfesionalIdAndFechaHora(dto.getProfesionalId(), dto.getFechaHora())) {
            throw new IllegalArgumentException("Ese horario ya está ocupado");
        }

        Cita cita = new Cita();

        Profesional profesional = profesionalRepository.findById(dto.getProfesionalId())
                .orElseThrow(() -> new RuntimeException("Profesional no encontrado"));

        Sede sede = sedeRepository.findById(dto.getSedeId())
                .orElseThrow(() -> new RuntimeException("Sede no encontrada"));

        cita.setUsuario(usuario);
        cita.setProfesional(profesional);
        cita.setSede(sede);
        cita.setFechaHora(dto.getFechaHora());
        cita.setEstado("AGENDADA");

        // Primero guardamos la cita sin importar el correo
        citaRepository.save(cita);

        // Luego intentamos enviar el correo, pero sin afectar la operación
        String asunto = "Confirmación de Cita Médica";
        String cuerpoHtml = "<h3>Estimado(a) " + usuario.getNombre() + ",</h3>"
                + "<p>Su cita ha sido agendada exitosamente con los siguientes datos:</p>"
                + "<ul>"
                + "<li><strong>Profesional:</strong> " + profesional.getNombre() + "</li>"
                + "<li><strong>Especialidad:</strong> " + profesional.getEspecialidad().getNombre() + "</li>"
                + "<li><strong>Fecha y hora:</strong> " + dto.getFechaHora().toString() + "</li>"
                + "<li><strong>Sede:</strong> " + sede.getNombre() + "</li>"
                + "</ul>"
                + "<p>Por favor, llegue con 15 minutos de anticipación.</p>"
                + "<p>Gracias por usar nuestro sistema de citas.</p>"
                + "<br><p><em>EPS CitaSalud</em></p>";

        try {
            emailService.enviarCorreoConfirmacion(usuario.getEmail(), asunto, cuerpoHtml);
        } catch (MessagingException e) {
            // Solo se registra el error, no se lanza excepción para que no afecte la agenda
            System.err.println("Error enviando correo: " + e.getMessage());
        }
    }



    // NUEVO: Obtener citas por usuario (email)
    public List<Cita> obtenerCitasPorUsuario(String emailUsuario) {
        Usuario usuario = usuarioRepository.findByEmail(emailUsuario)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        return citaRepository.findByUsuario_UsuarioId(usuario.getUsuarioId());
    }

    public void cancelarCita(Long citaId, String emailUsuario, CancelarCitaDto cancelarDto) {
        Cita cita = citaRepository.findById(citaId)
                .orElseThrow(() -> new IllegalArgumentException("Cita no encontrada"));

        Usuario usuario = usuarioRepository.findByEmail(emailUsuario)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));

        // Validar que la cita pertenezca al usuario que la quiere cancelar
        if (!cita.getUsuario().getUsuarioId().equals(usuario.getUsuarioId())) {
            throw new IllegalArgumentException("No tienes permiso para cancelar esta cita");
        }

        // Cambiar estado a CANCELADA
        cita.setEstado("CANCELADA");

        // Guardar motivoCancelacion. Revisar después si pongo texto libre o predeterminados
        if (cancelarDto.getMotivoCancelacion() != null && !cancelarDto.getMotivoCancelacion().isBlank()) {
            cita.setMotivoCancelacion(cancelarDto.getMotivoCancelacion());
        }

        // Si se pasa motivoId, buscar y asignar
        if (cancelarDto.getMotivoId() != null) {
            MotivoCancelacion motivo = motivoCancelacionRepository.findById(cancelarDto.getMotivoId())
                    .orElseThrow(() -> new IllegalArgumentException("Motivo de cancelación no encontrado"));
            cita.setMotivo(motivo);
        }

        citaRepository.save(cita);
    }

    public void modificarCita(Long citaId, String emailUsuario, ModificarCitaDto dto) {
        Cita cita = citaRepository.findById(citaId)
                .orElseThrow(() -> new IllegalArgumentException("Cita no encontrada"));

        Usuario usuario = usuarioRepository.findByEmail(emailUsuario)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));


        if (!cita.getUsuario().getUsuarioId().equals(usuario.getUsuarioId())) {
            throw new IllegalArgumentException("No tienes permiso para modificar esta cita");
        }

        if ("CANCELADA".equalsIgnoreCase(cita.getEstado())) {
            throw new IllegalArgumentException("No se puede modificar una cita cancelada");
        }


        if (citaRepository.existsByProfesional_ProfesionalIdAndFechaHora(
                cita.getProfesional().getProfesionalId(), dto.getNuevaFechaHora())) {
            throw new IllegalArgumentException("La nueva hora ya está ocupada por el profesional");
        }


        cita.setFechaHora(dto.getNuevaFechaHora());


        citaRepository.save(cita);
    }

    public Cita obtenerCitaPorIdYUsuario(Long id, String emailUsuario) {
        Optional<Cita> citaOpt = citaRepository.findById(id);

        if (citaOpt.isPresent()) {
            Cita cita = citaOpt.get();

            if (cita.getUsuario() != null &&
                    emailUsuario.equals(cita.getUsuario().getEmail())) {
                return cita;
            }
        }

        return null; // No encontrada o no pertenece al usuario autenticado
    }




}
