package com.catalogochileno.pagos.service;

import com.catalogochileno.pagos.client.OrdenesClienteFeign;
import com.catalogochileno.pagos.model.entity.TransaccionPago;
import com.catalogochileno.pagos.model.enums.EstadoTransaccion;
import com.catalogochileno.pagos.repository.TransaccionPagoRepositorio;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PagosServicio {

    private final TransaccionPagoRepositorio transaccionRepo;
    private final OrdenesClienteFeign ordenesCliente;

    @Transactional
    public String iniciarTransaccion(Long idOrden) {

        Object orden = ordenesCliente.obtenerOrden(idOrden);

        String tokenGenerado = "token_" + UUID.randomUUID().toString();

        TransaccionPago transaccion = TransaccionPago.builder()
                .idOrdenRef(idOrden)
                .tokenWs(tokenGenerado)
                .montoTotalClp(10000) 
                .estado(EstadoTransaccion.INICIADA)
                .build();

        transaccionRepo.save(transaccion);
        return tokenGenerado;
    }

    @Transactional
    public TransaccionPago confirmarTransaccion(String tokenWs) {
        TransaccionPago transaccion = transaccionRepo.findByTokenWs(tokenWs)
                .orElseThrow(() -> new RuntimeException("Transacción no encontrada para el token: " + tokenWs));

        // Simulación de respuesta exitosa de pasarela
        transaccion.aprobarTransaccion("AUTH_" + UUID.randomUUID().toString().substring(0, 8));
        return transaccionRepo.save(transaccion);
    }

    @Transactional
    public void anularTransaccion(String tokenWs) {
        TransaccionPago transaccion = transaccionRepo.findByTokenWs(tokenWs)
                .orElseThrow(() -> new RuntimeException("Transacción no encontrada para el token: " + tokenWs));

        transaccion.setEstado(EstadoTransaccion.ANULADA);
        transaccionRepo.save(transaccion);
    }
}