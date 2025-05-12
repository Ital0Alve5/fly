package com.dac.fly.shared.dto.events;

import java.time.LocalDateTime;

public record CancelledReservationEventDto(
        String codigo,
        LocalDateTime data,
        Double valor,
        Integer milhas_utilizadas,
        Integer quantidade_poltronas,
        Long codigo_cliente,
        String estado,
        String estado_anterior,
        String codigo_voo,
        String codigo_aeroporto_origem,
        String codigo_aeroporto_destino) {
}
