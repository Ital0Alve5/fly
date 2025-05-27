
package com.dac.fly.shared.dto.events;

public record MilesUpdatedEvent(
    String codigo,
    boolean success
) {}
