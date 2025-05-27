package com.dac.fly.clientservice.util;

public class DocumentUtils {

    public static String formatCpf(String cpf) {
        if (cpf == null) return null;
        String digits = cpf.replaceAll("\\D", "");
        if (digits.length() != 11) {
            throw new IllegalArgumentException("CPF deve conter 11 dígitos");
        }
        return digits.replaceFirst("(\\d{3})(\\d{3})(\\d{3})(\\d{2})", "$1.$2.$3-$4");
    }

    public static String formatCep(String cep) {
        if (cep == null) return null;
        String digits = cep.replaceAll("\\D", "");
        if (digits.length() != 8) {
            throw new IllegalArgumentException("CEP deve conter 8 dígitos");
        }
        return digits.replaceFirst("(\\d{5})(\\d{3})", "$1-$2");
    }

    public static String unformat(String document) {
        return document != null ? document.replaceAll("\\D", "") : null;
    }

    public static boolean isValidCpf(String cpf) {
        if (cpf == null) return false;
        String digits = unformat(cpf);
        return digits.length() == 11;
    }

    public static boolean isValidCep(String cep) {
        if (cep == null) return false;
        String digits = unformat(cep);
        return digits.length() == 8;
    }
}