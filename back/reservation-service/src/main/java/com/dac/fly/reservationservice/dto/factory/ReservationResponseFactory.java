package com.dac.fly.reservationservice.dto.factory;

import org.springframework.stereotype.Component;

import com.dac.fly.reservationservice.dto.response.ReservationResponseDto;
import com.dac.fly.reservationservice.entity.command.Reserva;
import com.dac.fly.reservationservice.enums.ReservationStatusEnum;

@Component
public class ReservationResponseFactory {

    public ReservationResponseDto fromCommandReserva(
            Reserva reserva,
            ReservationStatusEnum estado,
            String aeroportoOrigem,
            String aeroportoDestino) {

        return new ReservationResponseDto(
                reserva.getCodigo(),
                reserva.getDataReserva(),
                reserva.getValorPago(),
                reserva.getMilhasUtilizadas(),
                reserva.getQuantidadePoltronas(),
                reserva.getCodigoCliente(),
                estado.toString(),
                reserva.getCodigoVoo(),
                aeroportoOrigem,
                aeroportoDestino
        );
    }

    public ReservationResponseDto fromQueryReserva(com.dac.fly.reservationservice.entity.query.Reserva reserva) {
        return new ReservationResponseDto(
            reserva.getCodigo(),
            reserva.getDataReserva(),
            reserva.getValor(),
            reserva.getMilhasUtilizadas(),
            reserva.getQuantidadePoltronas(),
            reserva.getCodigoCliente(),
            reserva.getEstado(),
            reserva.getCodigoVoo(),
            reserva.getAeroportoOrigem(),
            reserva.getAeroportoDestino()
        );
    }
    }
