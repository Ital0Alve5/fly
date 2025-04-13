package com.dac.fly.reservationservice.repository.command;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dac.fly.reservationservice.entity.command.Reserva;

@Repository
public interface ReservaCommandRepository extends JpaRepository<Reserva, String> {}
