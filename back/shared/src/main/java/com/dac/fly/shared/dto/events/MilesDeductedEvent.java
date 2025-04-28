
package com.dac.fly.shared.dto.events;

public record MilesDeductedEvent(
    String reservationId,
    boolean success
) {}
