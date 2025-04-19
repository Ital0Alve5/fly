package com.dac.fly.reservationservice.repository.command;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dac.fly.reservationservice.entity.command.Historico;

@Repository
public interface HistoryRepository extends JpaRepository<Historico, Long> {
    List<Historico> findByCodigoReserva(String codigoReserva);
}
