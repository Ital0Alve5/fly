package com.dac.fly.shared.dto.request;

public record CreateReservationDto(
        Long codigo_cliente,
        Double valor,
        Integer milhas_utilizadas,
        Integer quantidade_poltronas,
        String codigo_voo,
        String codigo_aeroporto_origem,
        String codigo_aeroporto_destino) {
}
