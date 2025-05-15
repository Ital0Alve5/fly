
package com.dac.fly.shared.dto.command;

public record UpdateMilesCommand(
        String codigo_reserva,
        Long codigoCliente,
        Integer milhasUtilizadas,
        boolean isCompensate) {
}
