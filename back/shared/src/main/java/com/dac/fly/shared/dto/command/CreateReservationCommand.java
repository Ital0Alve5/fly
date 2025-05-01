package com.dac.fly.shared.dto.command;

public record CreateReservationCommand(
        String codigo,
        Long codigo_cliente,
        Double valor,
        Integer milhas_utilizadas,
        Integer quantidade_poltronas,
        String codigo_voo,
        String codigo_aeroporto_origem,
        String codigo_aeroporto_destino) {
}
