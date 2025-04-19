package com.dac.fly.reservationservice.service.query;

import java.util.List;

import org.springframework.stereotype.Service;

import com.dac.fly.reservationservice.dto.response.ReservationDto;
import com.dac.fly.reservationservice.entity.query.Reserva;
import com.dac.fly.reservationservice.enums.ReservationStatusEnum;
import com.dac.fly.reservationservice.repository.query.ReservaQueryRepository;

@Service
public class ReservationQueryService {

    private final ReservaQueryRepository reservaQueryRepository;

    public ReservationQueryService(ReservaQueryRepository reservaQueryRepository) {
        this.reservaQueryRepository = reservaQueryRepository;
    }

    public List<ReservationDto> getReservationByClientCode(Long clientCode) {
        List<Reserva> reservations = reservaQueryRepository.findBycodigoCliente(clientCode);

        return reservations.stream().map(r -> new ReservationDto(
                r.getCodigo(),
                r.getDataReserva(),
                r.getValor(),
                r.getMilhasUtilizadas(),
                r.getQuantidadePoltronas(),
                r.getCodigoCliente(),
                ReservationStatusEnum.valueOf(r.getEstado()))).toList();
    }

    public ReservationDto getReservationByCode(String code) {
        Reserva reservation = reservaQueryRepository.findById(code)
                .orElseThrow(() -> new RuntimeException("Reserva não encontrada: " + code));

        return ReservationDto.fromQuery(reservation);
    }
}
