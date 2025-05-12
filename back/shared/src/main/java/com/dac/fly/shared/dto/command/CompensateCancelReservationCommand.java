package com.dac.fly.shared.dto.command;

public record CompensateCancelReservationCommand(
    String codigoReserva,
    String estadoAnterior 
) {}