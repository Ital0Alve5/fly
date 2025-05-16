package com.dac.fly.employeeservice.controllers;

import java.util.List;

import com.dac.fly.employeeservice.util.DocumentUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.dac.fly.employeeservice.dto.response.EmployeeDto;
import com.dac.fly.employeeservice.service.EmployeeService;
import com.dac.fly.shared.dto.response.ApiResponse;

@RestController
@RequestMapping("/funcionarios")
public class EmployeeController {

    private final EmployeeService service;
    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService service, EmployeeService employeeService) {
        this.service = service;
        this.employeeService = employeeService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<EmployeeDto>>> findAll() {
        try {
            List<EmployeeDto> employees = service.getAllEmployees();

            return ResponseEntity.ok(ApiResponse.success(employees));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage(), 400));
        }
    }

    @GetMapping("/{codigo}")
    public ResponseEntity<ApiResponse<EmployeeDto>> getEmployeeByCodigo(@PathVariable Long codigo) {
        try {
            System.out.println("Chegou");
            EmployeeDto employee = employeeService.findEmployeeByCodigo(codigo);
            System.out.println("Buscou");
            return ResponseEntity.ok(ApiResponse.success(employee));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.error(e.getMessage(), 404));
        }
    }

    @GetMapping("/{email:.+}/email")
    public ResponseEntity<ApiResponse<EmployeeDto>> getEmployeeByEmail(@PathVariable String email) {
        try {
            EmployeeDto employee = employeeService.findEmployeeByEmail(email);
            return ResponseEntity.ok(ApiResponse.success(employee));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.error(e.getMessage(), 404));
        }
    }

    @GetMapping("/{cpf}/cpf")
    public ResponseEntity<ApiResponse<EmployeeDto>> getEmployeeByCpf(@PathVariable("cpf") String cpf) {
        try {
            EmployeeDto employee = employeeService.findEmployeeByCpf(cpf);
            return ResponseEntity.ok(ApiResponse.success(employee));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.error(e.getMessage(), 404));
        }
    }
}