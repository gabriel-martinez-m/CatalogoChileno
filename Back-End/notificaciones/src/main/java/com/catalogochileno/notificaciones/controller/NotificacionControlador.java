package com.catalogochileno.notificaciones.controller;

import com.catalogochileno.notificaciones.model.entity.RegistroNotificacion;
import com.catalogochileno.notificaciones.model.enums.TipoCorreo;
import com.catalogochileno.notificaciones.service.NotificacionServicio;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/notificaciones")
@RequiredArgsConstructor
public class NotificacionControlador {

    private final NotificacionServicio notificacionServicio;

    @PostMapping("/encolar")
    public ResponseEntity<RegistroNotificacion> encolar(@RequestBody SolicitudCorreoDto dto) {
        RegistroNotificacion registro = notificacionServicio.encolarCorreo(
                dto.getDestinatario(),
                dto.getTipo(),
                dto.getAsunto(),
                dto.getContenidoHtml()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(registro);
    }

    @PostMapping("/reintentar")
    public ResponseEntity<Void> reintentar() {
        notificacionServicio.reintentarFallidos();
        return ResponseEntity.ok().build();
    }

    @Data
    public static class SolicitudCorreoDto {
        @Email @NotBlank private String destinatario;
        @NotNull private TipoCorreo tipo;
        @NotBlank private String asunto;
        @NotBlank private String contenidoHtml;
    }
}