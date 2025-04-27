package com.dac.fly.flyservice.repository;

import java.time.OffsetDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dac.fly.flyservice.entity.Voo;

public interface VooRepository extends JpaRepository<Voo, String> {
    List<Voo> findByDataBetween(OffsetDateTime inicio, OffsetDateTime fim);

    List<Voo> findByDataBetweenAndAeroportoOrigemCodigoAndAeroportoDestinoCodigo(
            OffsetDateTime inicio, OffsetDateTime fim, String origem, String destino);
}
