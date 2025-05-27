package com.dac.fly.reservationservice.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;

@JsonFormat(shape = JsonFormat.Shape.STRING)
public enum ReservationStatusEnum {
    CRIADA,
    CHECK_IN,
    CANCELADA,
    CANCELADA_VOO,
    EMBARCADA,
    REALIZADA,
    NAO_REALIZADA;

    @JsonCreator
    public static ReservationStatusEnum fromValue(String value) {
        return switch (value.trim().toUpperCase()) {
            case "CHECK-IN" -> CHECK_IN;
            case "CANCELADA VOO", "CANCELADA-VOO" -> CANCELADA_VOO;
            case "NAO REALIZADA", "NAO-REALIZADA" -> NAO_REALIZADA;
            default -> valueOf(value.replace("-", "_").replace(" ", "_"));
        };
    }

    @Override
    public String toString() {
        return switch (this) {
            case CHECK_IN -> "CHECK-IN";
            case CANCELADA_VOO -> "CANCELADA VOO";
            case NAO_REALIZADA -> "NÃO REALIZADA";
            default -> name();
        };
    }
}
