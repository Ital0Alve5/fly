package com.dac.fly.shared.dto.events;

public record CompletedFlightEventDto(
    String codigo,
    String data,
    Double valor_passagem,
    Integer quantidade_poltronas_total,
    Integer quantidade_poltronas_ocupadas,
    String estadoAnterior,
    String estado,
    String codigo_aeroporto_origem,
    String codigo_aeroporto_destino
) {}
