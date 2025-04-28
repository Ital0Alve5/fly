
package com.dac.fly.shared.dto.start;

public record StartReservationDto(
    String reservationId,
    Long clientId,
    String flightCode,
    Integer usedMiles,
    Integer seatsReserved
) {}
