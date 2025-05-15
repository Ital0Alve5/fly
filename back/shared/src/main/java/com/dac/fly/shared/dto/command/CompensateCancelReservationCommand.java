package com.dac.fly.shared.dto.command;

public record CompensateCancelReservationCommand(
    String codigo_reserva,
    String estadoAnterior 
) {}