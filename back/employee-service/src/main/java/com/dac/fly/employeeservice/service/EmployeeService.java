package com.dac.fly.employeeservice.service;

import org.springframework.stereotype.Service;

import com.dac.fly.employeeservice.dto.request.CreateNewEmployeeDto;
import com.dac.fly.employeeservice.dto.request.UpdateEmployeeDto;
import com.dac.fly.employeeservice.dto.response.EmployeeDto;
import com.dac.fly.employeeservice.entity.Funcionario;
import com.dac.fly.employeeservice.repository.EmployeeRepository;
import java.util.List;
import java.util.Optional;
import static com.dac.fly.employeeservice.util.DocumentUtils.*;

@Service
public class EmployeeService {

    private final EmployeeRepository repository;

    public EmployeeService(EmployeeRepository repository) {
        this.repository = repository;
    }

    public List<EmployeeDto> getAllEmployees() {
        List<Funcionario> employees = repository.findAll();

        return employees.stream().map(e -> EmployeeDto.fromEntity(e)).toList();
    }

    public EmployeeDto createNewEmployee(CreateNewEmployeeDto newEmployeeDto) {
        Funcionario employee = new Funcionario();

        employee.setCpf(formatCpf(newEmployeeDto.cpf()));
        employee.setEmail(newEmployeeDto.email());
        employee.setNome(newEmployeeDto.nome());
        employee.setTelefone(formatTelefone(newEmployeeDto.telefone()));

        repository.save(employee);

        return EmployeeDto.fromEntity(employee);
    }

    public EmployeeDto deleteEmployee(Long employeeCode) {
        Optional<Funcionario> optionalFuncionario = repository.findById(employeeCode);

        optionalFuncionario.ifPresent(repository::delete);

        return EmployeeDto.fromEntity(optionalFuncionario.get());
    }

    public EmployeeDto updateEmployee(Long employeeCode, UpdateEmployeeDto employeeDto) {
        Optional<Funcionario> optionalFuncionario = repository.findById(employeeCode);

        return optionalFuncionario
                .map(employee -> {
                    if (employeeDto.cpf() != null)
                        employee.setCpf(formatCpf(employeeDto.cpf()));
                    if (employeeDto.email() != null)
                        employee.setEmail(employeeDto.email());
                    if (employeeDto.nome() != null)
                        employee.setNome(employeeDto.nome());
                    if (employeeDto.telefone() != null)
                        employee.setTelefone(formatTelefone(employeeDto.telefone()));

                    Funcionario updatedEmployee = repository.save(employee);
                    return EmployeeDto.fromEntity(updatedEmployee);
                }).orElseThrow(() -> new RuntimeException("Funcionário não encontrado."));
    }
}