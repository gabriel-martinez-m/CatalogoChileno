package com.catalogochileno.notificaciones.service;

import com.catalogochileno.notificaciones.model.entity.RegistroNotificacion;
import com.catalogochileno.notificaciones.model.enums.EstadoEnvio;
import com.catalogochileno.notificaciones.model.enums.TipoCorreo;
import com.catalogochileno.notificaciones.repository.NotificacionRepositorio;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificacionServicio {

    private final NotificacionRepositorio notificacionRepo;
    private final SmtpProveedor smtpProveedor;

    @Transactional
    public RegistroNotificacion encolarCorreo(String destinatario, TipoCorreo tipo, String asunto, String html) {
        RegistroNotificacion registro = RegistroNotificacion.builder()
                .correoDestinatario(destinatario)
                .tipo(tipo)
                .asunto(asunto)
                .contenidoHtml(html)
                .estado(EstadoEnvio.PENDIENTE)
                .build();

        return notificacionRepo.save(registro);
    }

    @Transactional
    @Scheduled(fixedRate = 60000)
    public void procesarEnviosPendientes() {
        List<RegistroNotificacion> pendientes = notificacionRepo.findByEstado(EstadoEnvio.PENDIENTE);
        for (RegistroNotificacion registro : pendientes) {
            Boolean exito = smtpProveedor.enviarCorreo(
                    registro.getCorreoDestinatario(),
                    registro.getAsunto(),
                    registro.getContenidoHtml()
            );
            if (Boolean.TRUE.equals(exito)) {
                registro.marcarEnviado();
            } else {
                registro.marcarFallido("Error de conexion SMTP");
            }
            notificacionRepo.save(registro);
        }
    }

    @Transactional
    public void reintentarFallidos() {
        List<RegistroNotificacion> fallidos = notificacionRepo.findByEstado(EstadoEnvio.FALLIDO);
        for (RegistroNotificacion registro : fallidos) {
            registro.setEstado(EstadoEnvio.PENDIENTE);
            notificacionRepo.save(registro);
        }
        procesarEnviosPendientes();
    }
}