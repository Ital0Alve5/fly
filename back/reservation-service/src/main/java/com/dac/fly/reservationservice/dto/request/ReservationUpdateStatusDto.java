package com.dac.fly.reservationservice.dto.request;

import com.dac.fly.reservationservice.enums.ReservationStatusEnum;

public record ReservationUpdateStatusDto(ReservationStatusEnum estado) {
}
