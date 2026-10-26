package com.catalogochileno.comisiones.repository;

import com.catalogochileno.comisiones.model.entity.SolicitudLiquidacion;
import com.catalogochileno.comisiones.model.enums.EstadoLiquidacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SolicitudLiquidacionRepositorio extends JpaRepository<SolicitudLiquidacion, Long> {
    List<SolicitudLiquidacion> findByEstado(EstadoLiquidacion estado);
}