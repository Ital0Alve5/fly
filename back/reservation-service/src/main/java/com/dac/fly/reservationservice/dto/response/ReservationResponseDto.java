package com.dac.fly.reservationservice.dto.response;

import java.time.OffsetDateTime;

public record ReservationResponseDto(String codigo,
        OffsetDateTime data,
        Double valor,
        Integer milhas_utilizadas,
        Integer quantidade_poltronas,
        Long codigo_cliente,
        String estado,
        String codigo_voo,
        String codigo_aeroporto_origem,
        String codigo_aeroporto_destino) {

}
