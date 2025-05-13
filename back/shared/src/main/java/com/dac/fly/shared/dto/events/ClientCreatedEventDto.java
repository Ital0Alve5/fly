package com.dac.fly.shared.dto.events;

public record ClientCreatedEventDto(Long codigo, String email, Boolean success) {
}
