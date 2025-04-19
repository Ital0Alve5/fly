package com.dac.fly.reservationservice.enums;

public enum ReservationStatusEnum {
    CRIADA,
    CHECK_IN,
    CANCELADA,
    CANCELADA_VOO,
    EMBARCADA,
    REALIZADA,
    NAO_REALIZADA;

    @Override
    public String toString() {
        return this.name().replace("_", "-");
    }
}
