package com.dac.fly.shared.dto.events;

import java.util.List;

public record FlightReservationsCompletedEventDto(
        String flightCode, List<String> reservationCodes) {
}
