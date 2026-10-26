package com.catalogochileno.licencias.repository;

import com.catalogochileno.licencias.model.entity.EnlaceDescarga;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface EnlaceDescargaRepositorio extends JpaRepository<EnlaceDescarga, Long> {
    Optional<EnlaceDescarga> findByUrlFirmadaAwsS3(String urlFirmadaAwsS3);
}