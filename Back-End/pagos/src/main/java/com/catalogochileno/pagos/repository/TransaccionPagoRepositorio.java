package com.catalogochileno.pagos.repository;

import com.catalogochileno.pagos.model.entity.TransaccionPago;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TransaccionPagoRepositorio extends JpaRepository<TransaccionPago, Long> {
    Optional<TransaccionPago> findByTokenWs(String tokenWs);
    Optional<TransaccionPago> findByIdOrdenRef(Long idOrdenRef);
}