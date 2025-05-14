package com.dac.fly.shared.config;

public final class RabbitConstants {
    private RabbitConstants() {
    }

    /** Exchange utilizada por todas as sagas */
    public static final String EXCHANGE = "reservation-saga-exchange";

    /** --- Contexto: Reserva (Reservation Saga) --- */
    public static final String CREATE_RESERVATION_CMD_QUEUE = "saga.create-reservation";
    public static final String CREATED_QUEUE = "saga.created-reservation";
    public static final String COMPENSATE_SEATS_CMD_QUEUE = "saga.compensate-seats-cmd";
    public static final String COMPENSATE_MILES_CMD_QUEUE = "saga.compensate-miles-cmd";
    public static final String COMPENSATE_CREATE_RESERVATION_CMD_QUEUE = "saga.compensate-create-reservation";

    public static final String CANCEL_RESERVATION_CMD_QUEUE = "saga.cancel-reservation";
    public static final String CANCELED_RESERVATION_RESP_QUEUE = "saga.reservation-canceled";
    public static final String COMPENSATE_CANCEL_RESERVATION_CMD_QUEUE = "saga.compensate-cancel-reservation";

    public static final String ROLLBACK_MILES_CMD_QUEUE = "saga.rollback-miles-cmd";
    public static final String ROLLBACK_SEATS_CMD_QUEUE        = "saga.rollback-seats-cmd";

    public static final String UPDATE_MILES_CMD_QUEUE = "saga.update-miles-cmd";
    public static final String UPDATE_MILES_RESP_QUEUE = "saga.update-miles-resp";

    public static final String UPDATE_SEATS_CMD_QUEUE = "saga.update-seats-cmd";
    public static final String UPDATE_SEATS_RESP_QUEUE = "saga.update-seats-resp";

    /** --- Contexto: Voo (Flight Saga) --- */
    public static final String CANCEL_FLIGHT_CMD_QUEUE = "saga.cancel-flight";
    public static final String FLIGHT_CANCELLED_RESP_QUEUE = "saga.flight-cancelled";
    public static final String COMPLETE_FLIGHT_CMD_QUEUE = "saga.complete-flight";
    public static final String FLIGHT_COMPLETED_RESP_QUEUE = "saga.flight-completed";
    public static final String FLIGHT_RESERVATIONS_CANCELLED_QUEUE = "saga.flight-reservations-cancelled";
    public static final String CANCEL_RESERVATION_BY_FLIGHT_CMD_QUEUE = "saga.flight-reservations-cancel";
    public static final String COMPENSATE_CANCEL_FLIGHT_CMD_QUEUE = "saga.compensate-cancel-flight";
    public static final String ROLLBACK_FLIGHT_CMD_QUEUE          = "saga.rollback-flight-cmd";
    public static final String COMPLETE_RESERVATION_BY_FLIGHT_CMD_QUEUE = "saga.flight-reservations-complete";
    public static final String FLIGHT_RESERVATIONS_COMPLETED_QUEUE = "saga.flight-reservations-completed";

    /** --- Contexto: Cliente (Client Saga) --- */
    public static final String CREATE_CLIENT_CMD_QUEUE = "saga.create-client-cmd";
    public static final String CREATE_CLIENT_RESP_QUEUE = "saga.create-client-resp";
    public static final String DELETE_CLIENT_CMD_QUEUE = "saga.delete-client-cmd";
    public static final String DELETE_CLIENT_RESP_QUEUE = "saga.delete-client-resp";

    /** --- Contexto: Funcionário (Employee Saga) --- */
    public static final String CREATE_EMPLOYEE_CMD_QUEUE = "saga.create-employee-cmd";
    public static final String CREATE_EMPLOYEE_RESP_QUEUE = "saga.create-employee-resp";
    public static final String UPDATE_EMPLOYEE_CMD_QUEUE = "saga.update-employee-cmd";
    public static final String UPDATE_EMPLOYEE_RESP_QUEUE = "saga.update-employee-resp";
    public static final String DELETE_EMPLOYEE_CMD_QUEUE = "saga.delete-employee-cmd";
    public static final String DELETE_EMPLOYEE_RESP_QUEUE = "saga.delete-employee-resp";

    /** --- Contexto: Autenticação (Auth Saga) --- */
    public static final String CREATE_USER_CMD_QUEUE = "saga.create-user-cmd";
    public static final String CREATE_USER_RESP_QUEUE = "saga.create-user-resp";
    public static final String UPDATE_USER_CMD_QUEUE = "saga.update-user-cmd";
    public static final String UPDATE_USER_RESP_QUEUE = "saga.update-user-resp";
    public static final String DELETE_USER_CMD_QUEUE = "saga.delete-user-cmd";
    public static final String DELETE_USER_RESP_QUEUE = "saga.delete-user-resp";
}
