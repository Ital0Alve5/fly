package com.dac.fly.reservationservice.service;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Service;

import com.dac.fly.reservationservice.entity.command.Reserva;
import com.dac.fly.reservationservice.repository.command.ReservaCommandRepository;
import com.dac.fly.shared.dto.command.CreateReservationCommand;

@Service
public class ReservationSagaService {

    private final ReservaCommandRepository reservaRepo;
    private final Set<String> processedReservations = ConcurrentHashMap.newKeySet();

    public ReservationSagaService(ReservaCommandRepository reservaRepo) {
        this.reservaRepo = reservaRepo;
    }

    public void createReservation(CreateReservationCommand cmd) {
        if (processedReservations.contains(cmd.reservationId())) {
            System.out.println("Reserva já existente (memória): " + cmd.reservationId());
            return;
        }

        Reserva reserva = new Reserva();
        reserva.setCodigo(cmd.reservationId());
        reserva.setCodigoCliente(cmd.clientId());
        reserva.setCodigoVoo(cmd.flightCode());
        reserva.setQuantidadePoltronas(cmd.seatsReserved());
        reservaRepo.save(reserva);

        processedReservations.add(cmd.reservationId());
    }
}
