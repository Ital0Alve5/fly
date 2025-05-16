package com.dac.fly.employeeservice.dto.response;

import com.dac.fly.employeeservice.entity.Funcionario;

public record EmployeeDto(Long codigo, String cpf, String email, String nome, String telefone, String tipo) {

    public static EmployeeDto fromEntity(Funcionario employee) {
        return new EmployeeDto(
                employee.getCodigo(),
                employee.getCpf(),
                employee.getEmail(),
                employee.getNome(),
                employee.getTelefone(),
                "FUNCIONARIO"
        );
    }
}