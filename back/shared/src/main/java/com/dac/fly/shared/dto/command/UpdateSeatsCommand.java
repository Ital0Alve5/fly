
package com.dac.fly.shared.dto.command;

public record UpdateSeatsCommand(
    String reservationId,
    String flightCode,
    Integer seatsReserved
) {}
