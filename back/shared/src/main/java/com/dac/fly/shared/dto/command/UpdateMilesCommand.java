
package com.dac.fly.shared.dto.command;

public record UpdateMilesCommand(
        String codigoReserva,
        Long codigoCliente,
        Integer milhasUtilizadas,
        boolean isCompensate) {
}
