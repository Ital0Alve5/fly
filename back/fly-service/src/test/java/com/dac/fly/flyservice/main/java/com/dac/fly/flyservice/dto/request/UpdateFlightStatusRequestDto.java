package com.dac.fly.flyservice.dto.request;

import com.dac.fly.flyservice.enums.ReservationStatusEnum;

public record UpdateFlightStatusRequestDto(ReservationStatusEnum estado) {
}