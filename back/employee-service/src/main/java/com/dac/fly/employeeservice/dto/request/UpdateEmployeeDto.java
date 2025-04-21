package com.dac.fly.employeeservice.dto.request;

public record UpdateEmployeeDto(Long codigo, String cpf, String email, String nome, String telefone, String senha) {

}
