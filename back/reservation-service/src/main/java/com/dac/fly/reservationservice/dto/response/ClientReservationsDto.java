package com.dac.fly.reservationservice.dto.response;

import java.time.LocalDateTime;

import com.dac.fly.reservationservice.enums.ReservationStatusEnum;

public record ClientReservationsDto(
        String codigo,
        LocalDateTime data,
        Double valor,
        Integer milhas_utilizadas,
        Integer quantidade_poltronas,
        Long codigo_cliente,
        ReservationStatusEnum estado) {
}
