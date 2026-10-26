package com.catalogochileno.licencias.controller;

import com.catalogochileno.licencias.model.entity.EnlaceDescarga;
import com.catalogochileno.licencias.model.entity.LicenciaCertificado;
import com.catalogochileno.licencias.service.LicenciaServicio;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/licencias")
@RequiredArgsConstructor
public class LicenciaControlador {

    private final LicenciaServicio licenciaServicio;

    @PostMapping
    public ResponseEntity<LicenciaCertificado> generarLicencia(@RequestBody SolicitudLicenciaDto solicitud) {
        LicenciaCertificado creada = licenciaServicio.generarLicencia(
                solicitud.getIdUsuario(),
                solicitud.getIdProducto(),
                solicitud.getIdOrden()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(creada);
    }

    @GetMapping("/validar/{codigoQr}")
    public ResponseEntity<Boolean> validarLicencia(@PathVariable String codigoQr) {
        return ResponseEntity.ok(licenciaServicio.validarLicenciaPublica(codigoQr));
    }

    @PostMapping("/{idLicencia}/enlaces")
    public ResponseEntity<EnlaceDescarga> generarEnlace(@PathVariable Long idLicencia) {
        return ResponseEntity.status(HttpStatus.CREATED).body(licenciaServicio.generarEnlaceDescarga(idLicencia));
    }

    @Data
    public static class SolicitudLicenciaDto {
        @NotNull private Long idUsuario;
        @NotNull private Long idProducto;
        @NotNull private Long idOrden;
    }
}