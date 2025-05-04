package com.dac.fly.saga.controllers;

import com.dac.fly.saga.service.EmployeeSagaOrchestrator;
import com.dac.fly.shared.dto.request.CreateNewEmployeeDto;
import com.dac.fly.shared.dto.request.UpdateEmployeeDto;
import com.dac.fly.shared.dto.response.ApiResponse;
import com.dac.fly.shared.dto.response.EmployeeDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/sagas/funcionarios")
public class EmployeeSagaController {
    private final EmployeeSagaOrchestrator orchestrator;

    public EmployeeSagaController(EmployeeSagaOrchestrator orchestrator) {
        this.orchestrator = orchestrator;
    }

    @PostMapping()
    public ResponseEntity<ApiResponse<EmployeeDto>> createEmployee(
            @RequestBody CreateNewEmployeeDto dto
    ){
       try {
           System.out.println("Received create employee request");
           EmployeeDto employee = orchestrator.createEmployeeSaga(dto);
           return ResponseEntity
                   .ok(ApiResponse.success(employee));
       }catch (RuntimeException e){
           System.out.println("Erro ao criar funcionario: " + e.getMessage());
           return ResponseEntity
                   .status(HttpStatus.NOT_FOUND)
                   .body(ApiResponse.error(e.getMessage(), HttpStatus.NOT_FOUND.value()));

       }
    }

    @PutMapping("/{codigoFuncionario}")
    public ResponseEntity<ApiResponse<EmployeeDto>> updateEmployee(
            @RequestBody UpdateEmployeeDto dto
    ){
        try {
            System.out.println("Received update employee request");
            EmployeeDto employee = orchestrator.updateEmployeeSaga(dto);
            return ResponseEntity
                    .ok(ApiResponse.success(employee));
        }catch (RuntimeException e){
            System.out.println("Erro ao criar funcionario: " + e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(e.getMessage(), HttpStatus.NOT_FOUND.value()));

        }
    }

    @DeleteMapping("/{codigoFuncionario}")
    public ResponseEntity<ApiResponse<EmployeeDto>> deleteEmployee(
            @PathVariable String codigoFuncionario){
        try {
            System.out.println("Received delete employee request");
            EmployeeDto employee = orchestrator.deleteEmployeeSaga(Long.parseLong(codigoFuncionario));
            System.out.println(employee);
            return ResponseEntity
                    .ok(ApiResponse.success(employee));
        }catch (RuntimeException e){
            System.out.println("Erro ao deletar funcionario: " + e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(e.getMessage(), HttpStatus.NOT_FOUND.value()));

        }
    }
}
