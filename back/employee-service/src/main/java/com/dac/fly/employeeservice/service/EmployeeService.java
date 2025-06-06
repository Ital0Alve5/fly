package com.dac.fly.employeeservice.service;

import com.dac.fly.shared.dto.command.CreateEmployeeCommandDto;
import com.dac.fly.shared.dto.command.UpdateEmployeeCommandDto;
import org.springframework.stereotype.Service;

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

    public EmployeeDto findEmployeeByCodigo(Long codigo) {
        Funcionario employee = repository.findByCodigo(codigo)
                .orElseThrow(() -> new RuntimeException("funcionario não encontrado com código: " + codigo));

        return EmployeeDto.fromEntity(employee);
    }

    public EmployeeDto findEmployeeByEmail(String email) {
        Funcionario employee = repository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("funcionario não encontrado com email: " + email));

        return EmployeeDto.fromEntity(employee);
    }

    public EmployeeDto findEmployeeByCpf(String cpf) {
        Funcionario employee = repository.findByCpf(cpf)
                .orElseThrow(() -> new RuntimeException("funcionario não encontrado com cpf: " + cpf));

        return EmployeeDto.fromEntity(employee);
    }

    public EmployeeDto createNewEmployee(CreateEmployeeCommandDto newEmployeeDto) {
        Funcionario employee = new Funcionario();

        employee.setCpf(newEmployeeDto.cpf());
        employee.setEmail(newEmployeeDto.email());
        employee.setNome(newEmployeeDto.nome());
        employee.setTelefone(formatTelefone(newEmployeeDto.telefone()));

        repository.save(employee);

        return EmployeeDto.fromEntity(employee);
    }

    public EmployeeDto deleteEmployee(Long codigo) {
        Funcionario emp = repository.findById(codigo)
                .orElseThrow(() -> new RuntimeException("Funcionário não encontrado."));

        repository.delete(emp);
        return EmployeeDto.fromEntity(emp);
    }

    public EmployeeDto updateEmployee(Long employeeCode, UpdateEmployeeCommandDto employeeDto) {
        Optional<Funcionario> optionalFuncionario = repository.findById(employeeCode);

        return optionalFuncionario
                .map(employee -> {
                    if (employeeDto.cpf() != null)
                        employee.setCpf(employeeDto.cpf());
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