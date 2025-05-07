package com.dac.fly.shared.dto.events;

public record EmployeeUpdatedEventDto(String email, Long codigo, Boolean success) {
}
