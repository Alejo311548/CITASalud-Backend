package com.eps.citas.service;


import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void enviarCorreoConfirmacion(String destinatario, String asunto, String cuerpoHtml) throws MessagingException {
        MimeMessage mensaje = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mensaje, true, "UTF-8");

        helper.setFrom("citasaludeps@gmail.com");
        helper.setTo(destinatario);
        helper.setSubject(asunto);
        helper.setText(cuerpoHtml, true);

        mailSender.send(mensaje);
    }
}

