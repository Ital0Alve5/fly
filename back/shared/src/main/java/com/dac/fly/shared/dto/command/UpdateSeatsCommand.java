
package com.dac.fly.shared.dto.command;

public record UpdateSeatsCommand(
        String codigo_reserva,
        String codigoVoo,
        Integer quantidade_poltronas,
        boolean isCompensate) {
}
