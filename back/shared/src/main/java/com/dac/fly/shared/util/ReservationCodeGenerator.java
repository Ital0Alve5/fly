package com.dac.fly.shared.util;

import java.security.SecureRandom;

public class ReservationCodeGenerator {

    private static final SecureRandom RANDOM = new SecureRandom();
    private static final String LETTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

    public static String generateReservationCode() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 3; i++) {
            sb.append(LETTERS.charAt(RANDOM.nextInt(LETTERS.length())));
        }
        for (int i = 0; i < 3; i++) {
            sb.append(RANDOM.nextInt(10));
        }
        return sb.toString();
    }
}
