package com.catalogochileno.pagos.controller;

import com.catalogochileno.pagos.model.entity.TransaccionPago;
import com.catalogochileno.pagos.service.PagosServicio;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/pagos")
@RequiredArgsConstructor
public class PagosControlador {

    private final PagosServicio pagosServicio;

    @PostMapping("/iniciar")
    public ResponseEntity<String> iniciarTransaccion(@RequestBody SolicitudPagoDto dto) {
        String token = pagosServicio.iniciarTransaccion(dto.getIdOrden());
        return ResponseEntity.status(HttpStatus.CREATED).body(token);
    }

    @PostMapping("/confirmar")
    public ResponseEntity<TransaccionPago> confirmarTransaccion(@RequestBody ConfirmacionPagoDto dto) {
        TransaccionPago confirmada = pagosServicio.confirmarTransaccion(dto.getTokenWs());
        return ResponseEntity.ok(confirmada);
    }

    @PostMapping("/anular")
    public ResponseEntity<Void> anularTransaccion(@RequestBody ConfirmacionPagoDto dto) {
        pagosServicio.anularTransaccion(dto.getTokenWs());
        return ResponseEntity.ok().build();
    }

    @Data
    public static class SolicitudPagoDto {
        @NotNull private Long idOrden;
    }

    @Data
    public static class ConfirmacionPagoDto {
        @NotBlank private String tokenWs;
    }
}
