package com.dac.fly.shared.dto.command;

public record CompensateCancelFlightCommand(
    String flightCode,
    String estadoAnterior
) {}
