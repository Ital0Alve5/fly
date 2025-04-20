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
        return ReservationStatusEnum.valueOf(value.replace("-", "_"));
    }

    @Override
    public String toString() {
        return this.name().replace("_", "-");
    }
}
