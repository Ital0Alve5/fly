package com.dac.fly.reservationservice.dto.events;

import com.dac.fly.reservationservice.enums.ReservationStatusEnum;

public record ReservationStatusUpdatedEventDto(String reservationCode, ReservationStatusEnum newStatus) {
    
}
