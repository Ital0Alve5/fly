package com.dac.fly.flyservice.dto.request;

public record CreateNewFlightRequestDto(
                String data,
                Double valor_passagem,
                Integer quantidade_poltronas_total,
                Integer quantidade_poltronas_ocupadas,
                String codigo_aeroporto_origem,
                String codigo_aeroporto_destino) {
}
