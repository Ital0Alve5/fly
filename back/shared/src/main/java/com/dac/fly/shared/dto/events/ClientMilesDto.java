package com.dac.fly.shared.dto.events;

public record ClientMilesDto(
        String codigoReserva,
        Long codigoCliente,
        Integer milhasUtilizadas) {
}
