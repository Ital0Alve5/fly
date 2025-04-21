package com.dac.fly.employeeservice.util;

public class DocumentUtils {

    public static String formatCpf(String cpf) {
        if (cpf == null)
            return null;

        String onlyDigits = cpf.replaceAll("\\D", "");
        if (onlyDigits.length() != 11)
            throw new IllegalArgumentException("CPF inválido");

        return onlyDigits.replaceFirst("(\\d{3})(\\d{3})(\\d{3})(\\d{2})", "$1.$2.$3-$4");
    }

    public static String formatTelefone(String telefone) {
        if (telefone == null)
            return null;

        String onlyDigits = telefone.replaceAll("\\D", "");
        if (onlyDigits.length() != 11)
            throw new IllegalArgumentException("Telefone inválido");

        return onlyDigits.replaceFirst("(\\d{2})(\\d{5})(\\d{4})", "($1) $2-$3");
    }
}
