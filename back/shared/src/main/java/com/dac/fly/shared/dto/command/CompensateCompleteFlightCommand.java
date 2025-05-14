package com.dac.fly.shared.dto.command;

public record CompensateCompleteFlightCommand(
    String flightCode,
    String estadoAnterior
) {}
