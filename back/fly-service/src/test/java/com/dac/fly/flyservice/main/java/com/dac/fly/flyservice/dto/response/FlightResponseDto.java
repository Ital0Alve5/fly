package com.dac.fly.flyservice.dto.response;

import com.dac.fly.flyservice.enums.ReservationStatusEnum;

public record FlightResponseDto(
        String codigo,
        String data,
        Double valor_passagem,
        Integer quantidade_poltronas_total,
        Integer quantidade_poltronas_ocupadas,
        ReservationStatusEnum estado,
        String codigo_aeroporto_origem,
        String codigo_aeroporto_destino) {
}
