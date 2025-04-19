package com.dac.fly.reservationservice.repository.query;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dac.fly.reservationservice.entity.query.Reserva;

@Repository
public interface ReservaQueryRepository extends JpaRepository<Reserva, String> {
    List<Reserva> findBycodigoCliente(Long codigoCliente);

    Reserva findBycodigo(String code);
}
