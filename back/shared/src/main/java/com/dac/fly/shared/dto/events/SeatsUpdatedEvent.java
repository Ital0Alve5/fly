package com.dac.fly.shared.dto.events;

public record SeatsUpdatedEvent(
        String codigo,
        boolean success) {
}