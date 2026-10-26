package com.catalogochileno.notificaciones.repository;

import com.catalogochileno.notificaciones.model.entity.RegistroNotificacion;
import com.catalogochileno.notificaciones.model.enums.EstadoEnvio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificacionRepositorio extends JpaRepository<RegistroNotificacion, Long> {
    List<RegistroNotificacion> findByEstado(EstadoEnvio estado);
    List<RegistroNotificacion> findByCorreoDestinatario(String correo);
}