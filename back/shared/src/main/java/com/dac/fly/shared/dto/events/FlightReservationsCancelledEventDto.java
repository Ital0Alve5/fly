package com.dac.fly.shared.dto.events;

import java.util.List;

public record FlightReservationsCancelledEventDto(
        String flightCode,
        List<ClientMilesDto> refunds) {
}
