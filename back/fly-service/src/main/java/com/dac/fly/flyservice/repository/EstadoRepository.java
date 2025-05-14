package com.dac.fly.flyservice.repository;

import java.util.Optional;

import com.dac.fly.flyservice.enums.FlightStatusEnum;
import org.springframework.data.jpa.repository.JpaRepository;

import com.dac.fly.flyservice.entity.Estado;

public interface EstadoRepository extends JpaRepository<Estado, Long> {
    Optional<Estado> findByNome(FlightStatusEnum nome);
}
