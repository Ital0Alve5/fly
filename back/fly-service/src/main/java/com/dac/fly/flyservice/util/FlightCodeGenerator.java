package com.dac.fly.flyservice.util;

import java.security.SecureRandom;

public class FlightCodeGenerator {

    private static final SecureRandom RANDOM = new SecureRandom();
    private static final String PREFIX = "TADS";

    public static String generateReservationCode() {
        int number = RANDOM.nextInt(10_000);
        return String.format("%s%04d", PREFIX, number);
    }
}
