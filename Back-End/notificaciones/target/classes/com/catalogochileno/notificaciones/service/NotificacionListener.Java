package com.catalogochileno.notificaciones.service;

import com.catalogochileno.notificaciones.model.enums.TipoCorreo;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificacionListener {

    private final NotificacionServicio notificacionServicio;

    @EventListener
    public void recibirEventoCompra(Object evento) {
        notificacionServicio.encolarCorreo(
                "comprador@ejemplo.cl",
                TipoCorreo.CONFIRMACION_COMPRA,
                "Confirmación de Compra",
                "<h1>Gracias por tu compra</h1>"
        );
    }

    @EventListener
    public void recibirEventoRegistro(Object evento) {
        notificacionServicio.encolarCorreo(
                "usuario@ejemplo.cl",
                TipoCorreo.BIENVENIDA_USUARIO,
                "Bienvenido",
                "<h1>Bienvenido a la plataforma</h1>"
        );
    }
}