package com.catalogochileno.licencias.service;

import com.catalogochileno.licencias.client.CatalogoClienteFeign;
import com.catalogochileno.licencias.model.entity.EnlaceDescarga;
import com.catalogochileno.licencias.model.entity.LicenciaCertificado;
import com.catalogochileno.licencias.model.enums.EstadoLicencia;
import com.catalogochileno.licencias.repository.EnlaceDescargaRepositorio;
import com.catalogochileno.licencias.repository.LicenciaRepositorio;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class LicenciaServicio {

    private final LicenciaRepositorio licenciaRepo;
    private final EnlaceDescargaRepositorio enlaceRepo;
    private final CatalogoClienteFeign catalogoCliente;

    @Transactional
    public LicenciaCertificado generarLicencia(Long idUsuario, Long idProducto, Long idOrden) {
        Object metadatosProducto = catalogoCliente.obtenerMetadatosLicencia(idProducto);

        LicenciaCertificado licencia = LicenciaCertificado.builder()
                .codigoUnicoQr(UUID.randomUUID().toString())
                .idUsuarioCompradorRef(idUsuario)
                .idProductoRef(idProducto)
                .idOrdenRef(idOrden)
                .urlPdfGenerado("https://s3.amazonaws.com/bucket-licencias/" + UUID.randomUUID() + ".pdf")
                .estado(EstadoLicencia.ACTIVA)
                .build();

        return licenciaRepo.save(licencia);
    }

    @Transactional(readOnly = true)
    public Boolean validarLicenciaPublica(String codigoQr) {
        return licenciaRepo.findByCodigoUnicoQr(codigoQr)
                .map(LicenciaCertificado::esValida)
                .orElse(false);
    }

    @Transactional
    public EnlaceDescarga generarEnlaceDescarga(Long idLicencia) {
        LicenciaCertificado licencia = licenciaRepo.findById(idLicencia)
                .orElseThrow(() -> new RuntimeException("Licencia no encontrada"));

        if (!licencia.esValida()) {
            throw new IllegalStateException("Licencia revocada.");
        }

        EnlaceDescarga enlace = EnlaceDescarga.builder()
                .licencia(licencia)
                .urlFirmadaAwsS3("https://s3.amazonaws.com/bucket-downloads/" + UUID.randomUUID())
                .fechaExpiracion(LocalDateTime.now().plusHours(24))
                .estaUsado(false)
                .build();

        return enlaceRepo.save(enlace);
    }
}