package com.dac.fly.reservationservice.service.query;

import java.util.List;

import org.springframework.stereotype.Service;

import com.dac.fly.reservationservice.dto.response.ReservationResponseDto;
import com.dac.fly.reservationservice.entity.query.Reserva;
import com.dac.fly.reservationservice.repository.query.ReservaQueryRepository;
import com.dac.fly.reservationservice.dto.factory.ReservationResponseFactory;

@Service
public class ReservationQueryService {

    private final ReservaQueryRepository reservaQueryRepository;
    private final ReservationResponseFactory responseFactory;

    public ReservationQueryService(ReservaQueryRepository reservaQueryRepository,
            ReservationResponseFactory responseFactory) {
        this.reservaQueryRepository = reservaQueryRepository;
        this.responseFactory = responseFactory;
    }

    public List<ReservationResponseDto> getReservationByClientCode(Long clientCode) {
        List<Reserva> reservations = reservaQueryRepository.findBycodigoCliente(clientCode);
        return reservations.stream().map(responseFactory::fromQueryReserva).toList();
    }

    public ReservationResponseDto getReservationByCode(String code) {
        Reserva reservation = reservaQueryRepository.findById(code)
                .orElseThrow(() -> new RuntimeException("Reserva não encontrada: " + code));
        return responseFactory.fromQueryReserva(reservation);
    }
}
