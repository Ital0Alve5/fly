package com.dac.fly.flyservice.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dac.fly.flyservice.entity.Estado;
import com.dac.fly.flyservice.enums.ReservationStatusEnum;

public interface EstadoRepository extends JpaRepository<Estado, Long> {
    Optional<Estado> findByNome(ReservationStatusEnum nome);
}
