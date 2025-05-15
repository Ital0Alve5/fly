package com.dac.fly.shared.dto.events;

public record ClientMilesDto(
        String codigo_reserva,
        Long codigoCliente,
        Integer milhasUtilizadas,
        String estadoAnterior) {
}
