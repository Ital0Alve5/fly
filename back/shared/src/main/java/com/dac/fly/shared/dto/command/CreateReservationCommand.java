
package com.dac.fly.shared.dto.command;

public record CreateReservationCommand(
    String reservationId,
    Long clientId,
    String flightCode,
    Integer seatsReserved
) {}
