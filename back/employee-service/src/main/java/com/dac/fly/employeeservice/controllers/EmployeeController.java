package com.dac.fly.employeeservice.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dac.fly.employeeservice.dto.request.CreateNewEmployeeDto;
import com.dac.fly.employeeservice.dto.request.UpdateEmployeeDto;
import com.dac.fly.employeeservice.dto.response.ApiResponse;
import com.dac.fly.employeeservice.dto.response.EmployeeDto;
import com.dac.fly.employeeservice.service.EmployeeService;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/funcionarios")
public class EmployeeController {

    private final EmployeeService service;

    public EmployeeController(EmployeeService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<EmployeeDto>> create(@RequestBody CreateNewEmployeeDto request) {
        try {
            EmployeeDto employees = service.createNewEmployee(request);

            return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(employees));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage(), 400));
        }
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

    @DeleteMapping("/{codigoFuncionario}")
    public ResponseEntity<ApiResponse<EmployeeDto>> delete(@PathVariable Long codigoFuncionario) {
        try {
            EmployeeDto employee = service.deleteEmployee(codigoFuncionario);

            return ResponseEntity.ok(ApiResponse.success(employee));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage(), 400));
        }
    }

    @PutMapping("/{codigoFuncionario}")
    public ResponseEntity<ApiResponse<EmployeeDto>> update(@PathVariable Long codigoFuncionario,
            @RequestBody UpdateEmployeeDto request) {
        try {
            EmployeeDto employee = service.updateEmployee(codigoFuncionario, request);

            return ResponseEntity.ok(ApiResponse.success(employee));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage(), 400));
        }
    }
}
