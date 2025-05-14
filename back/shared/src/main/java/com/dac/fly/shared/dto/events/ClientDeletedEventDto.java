package com.dac.fly.shared.dto.events;

public record ClientDeletedEventDto(Long codigo, String email, Boolean success) {
}
