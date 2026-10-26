package com.catalogochileno.comisiones.controller;

import com.catalogochileno.comisiones.model.entity.SolicitudLiquidacion;
import com.catalogochileno.comisiones.service.ComisionesServicio;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/comisiones")
@RequiredArgsConstructor
public class ComisionesControlador {

    private final ComisionesServicio comisionesServicio;

    @PostMapping("/ventas")
    public ResponseEntity<Void> registrarVenta(@RequestBody RegistroVentaDto dto) {
        comisionesServicio.registrarVenta(dto.getIdArtista(), dto.getIdOrden(), dto.getMontoTotal());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/liquidaciones/solicitar")
    public ResponseEntity<SolicitudLiquidacion> solicitarLiquidacion(@RequestBody SolicitudLiquidacionDto dto) {
        SolicitudLiquidacion solicitud = comisionesServicio.solicitarLiquidacion(dto.getIdArtista(), dto.getMonto());
        return ResponseEntity.status(HttpStatus.CREATED).body(solicitud);
    }

    @PostMapping("/liquidaciones/{idSolicitud}/procesar")
    public ResponseEntity<Void> procesarLiquidacion(@PathVariable Long idSolicitud) {
        comisionesServicio.procesarLiquidacion(idSolicitud);
        return ResponseEntity.ok().build();
    }

    @Data
    public static class RegistroVentaDto {
        @NotNull private Long idArtista;
        @NotNull private Long idOrden;
        @NotNull private Integer montoTotal;
    }

    @Data
    public static class SolicitudLiquidacionDto {
        @NotNull private Long idArtista;
        @NotNull private Integer monto;
    }
}
