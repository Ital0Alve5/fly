package com.dac.fly.employeeservice.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dac.fly.employeeservice.dto.response.EmployeeDto;
import com.dac.fly.employeeservice.service.EmployeeService;
import com.dac.fly.shared.dto.response.ApiResponse;

@RestController
@RequestMapping("/funcionarios")
public class EmployeeController {

    private final EmployeeService service;

    public EmployeeController(EmployeeService service) {
        this.service = service;
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

}