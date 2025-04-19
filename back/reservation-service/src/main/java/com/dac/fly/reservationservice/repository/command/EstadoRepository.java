package com.dac.fly.reservationservice.repository.command;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dac.fly.reservationservice.entity.command.Estado;

@Repository
public interface EstadoRepository extends JpaRepository<Estado, Long> {
    Optional<Estado> findByNome(String nome);
}
