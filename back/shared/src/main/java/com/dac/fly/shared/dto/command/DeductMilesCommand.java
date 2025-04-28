
package com.dac.fly.shared.dto.command;

public record DeductMilesCommand(
    String reservationId,
    Long clientId,
    Integer miles
) {}
