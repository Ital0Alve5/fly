package com.dac.fly.shared.config;

public final class RabbitConstants {
    private RabbitConstants() {
    }

    public static final String EXCHANGE = "reservation-saga-exchange";

    public static final String RES_QUEUE = "saga.create-reservation";

    public static final String CREATED_QUEUE = "saga.created-reservation";

    public static final String CANCEL_QUEUE = "saga.cancel-reservation";
    public static final String CANCELED_QUEUE = "saga.reservation-canceled";

    public static final String UPDATE_MILES_CMD_QUEUE = "saga.update-miles-cmd";
    public static final String UPDATE_MILES_RESP_QUEUE = "saga.update-miles-resp";

    public static final String UPDATE_SEATS_CMD_QUEUE = "saga.update-seats-cmd";
    public static final String UPDATE_SEATS_RESP_QUEUE = "saga.update-seats-resp";

    public static final String CANCEL_FLIGHT_QUEUE = "saga.cancel-flight";
    public static final String FLIGHT_CANCELLED_QUEUE = "saga.flight-cancelled";

    public static final String FLIGHT_RESERVATIONS_CANCELLED_QUEUE = "saga.flight-reservations-cancelled";
}
