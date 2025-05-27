package com.dac.fly.shared.dto.events;

public record EmployeeCreatedEventDto(String email, Long codigo, Boolean success) {
}
