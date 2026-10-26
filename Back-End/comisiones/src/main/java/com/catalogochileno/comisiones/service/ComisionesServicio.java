package com.catalogochileno.comisiones.service;

import com.catalogochileno.comisiones.client.UsuarioClienteFeign;
import com.catalogochileno.comisiones.model.entity.LedgerArtista;
import com.catalogochileno.comisiones.model.entity.Movimiento;
import com.catalogochileno.comisiones.model.entity.SolicitudLiquidacion;
import com.catalogochileno.comisiones.model.enums.EstadoLiquidacion;
import com.catalogochileno.comisiones.model.enums.TipoMovimiento;
import com.catalogochileno.comisiones.repository.LedgerRepositorio;
import com.catalogochileno.comisiones.repository.SolicitudLiquidacionRepositorio;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ComisionesServicio {

    private final LedgerRepositorio ledgerRepo;
    private final SolicitudLiquidacionRepositorio solicitudRepo;
    private final UsuarioClienteFeign usuarioCliente;

    @Transactional
    public void registrarVenta(Long idArtista, Long idOrden, Integer montoTotal) {
        LedgerArtista ledger = ledgerRepo.findByIdUsuarioArtistaRef(idArtista)
                .orElseGet(() -> ledgerRepo.save(LedgerArtista.builder()
                        .idUsuarioArtistaRef(idArtista)
                        .saldoDisponibleClp(0)
                        .saldoRetenidoClp(0)
                        .build()));

        // Aplicar crédito al ledger del artista
        ledger.sumarCredito(montoTotal);

        Movimiento mov = Movimiento.builder()
                .ledger(ledger)
                .tipo(TipoMovimiento.CREDITO_VENTA)
                .montoClp(montoTotal)
                .idOrdenRef(idOrden)
                .build();

        ledger.getMovimientos().add(mov);
        ledgerRepo.save(ledger);
    }

    @Transactional
    public SolicitudLiquidacion solicitarLiquidacion(Long idArtista, Integer monto) {
        LedgerArtista ledger = ledgerRepo.findByIdUsuarioArtistaRef(idArtista)
                .orElseThrow(() -> new RuntimeException("No se encontró el ledger para el artista: " + idArtista));

        // Retener los fondos mientras se procesa la solicitud
        ledger.retenerFondos(monto);

        Movimiento mov = Movimiento.builder()
                .ledger(ledger)
                .tipo(TipoMovimiento.DEBITO_LIQUIDACION)
                .montoClp(monto)
                .build();

        ledger.getMovimientos().add(mov);
        ledgerRepo.save(ledger);

        SolicitudLiquidacion solicitud = SolicitudLiquidacion.builder()
                .idUsuarioArtistaRef(idArtista)
                .montoSolicitadoClp(monto)
                .estado(EstadoLiquidacion.PENDIENTE)
                .build();

        return solicitudRepo.save(solicitud);
    }

    @Transactional
    public void procesarLiquidacion(Long idSolicitud) {
        SolicitudLiquidacion solicitud = solicitudRepo.findById(idSolicitud)
                .orElseThrow(() -> new RuntimeException("Solicitud de liquidación no encontrada: " + idSolicitud));

        // Consultar datos bancarios del usuario antes de transferir
        Object datosBancarios = usuarioCliente.obtenerDatosBancarios(solicitud.getIdUsuarioArtistaRef());

        LedgerArtista ledger = ledgerRepo.findByIdUsuarioArtistaRef(solicitud.getIdUsuarioArtistaRef())
                .orElseThrow(() -> new RuntimeException("Ledger no encontrado"));

        // Liberar los fondos del saldo retenido y marcar como transferida
        ledger.liberarFondos(solicitud.getMontoSolicitadoClp());
        ledgerRepo.save(ledger);

        solicitud.marcarComoTransferida();
        solicitudRepo.save(solicitud);
    }
}