
package com.dac.fly.shared.dto.command;

public record UpdateSeatsCommand(
        String codigoReserva,
        String codigoVoo,
        Integer quantidadePoltronas,
        boolean isCompensate) {
}
