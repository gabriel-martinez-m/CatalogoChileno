package com.catalogochileno.licencias.repository;

import com.catalogochileno.licencias.model.entity.LicenciaCertificado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface LicenciaRepositorio extends JpaRepository<LicenciaCertificado, Long> {
    Optional<LicenciaCertificado> findByCodigoUnicoQr(String codigoUnicoQr);
    List<LicenciaCertificado> findByIdUsuarioCompradorRef(Long idUsuarioCompradorRef);
}