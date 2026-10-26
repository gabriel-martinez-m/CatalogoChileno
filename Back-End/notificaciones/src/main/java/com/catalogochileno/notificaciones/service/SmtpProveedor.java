package com.catalogochileno.notificaciones.service;

import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SmtpProveedor {

    @Value("${spring.mail.username:noreply@catalogochileno.cl}")
    private String remitente;

    private final JavaMailSender mailSender;

    public Boolean enviarCorreo(String destino, String asunto, String html) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setFrom(remitente);
            helper.setTo(destino);
            helper.setSubject(asunto);
            helper.setText(html, true);
            
            mailSender.send(message);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}