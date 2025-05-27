package com.dac.fly.saga.controllers;

import com.dac.fly.saga.service.EmployeeSagaOrchestrator;
import com.dac.fly.shared.dto.request.CreateNewEmployeeDto;
import com.dac.fly.shared.dto.request.UpdateEmployeeDto;
import com.dac.fly.shared.dto.response.ApiResponse;
import com.dac.fly.shared.dto.response.EmployeeDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.file.Path;

@RestController
@RequestMapping("/sagas/funcionarios")
public class EmployeeSagaController {
    private final EmployeeSagaOrchestrator orchestrator;

    public EmployeeSagaController(EmployeeSagaOrchestrator orchestrator) {
        this.orchestrator = orchestrator;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<EmployeeDto>> createEmployee(
            @RequestBody CreateNewEmployeeDto dto
    ) {
        try {
            EmployeeDto employee = orchestrator.createEmployeeSaga(dto);
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(ApiResponse.success(employee));
        } catch (IllegalArgumentException e) {
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body(ApiResponse.error(
                            e.getMessage(),
                            HttpStatus.CONFLICT.value()
                    ));
        }
    }

    @PutMapping("/{codigoFuncionario}")
    public ResponseEntity<ApiResponse<EmployeeDto>> updateEmployee(
            @RequestBody UpdateEmployeeDto dto,
            @PathVariable String codigoFuncionario
    ){
        try {
            Long codigo = Long.parseLong(codigoFuncionario);
            UpdateEmployeeDto dtoComCodigo = new UpdateEmployeeDto(
                    codigo,
                    dto.cpf(),
                    dto.email(),
                    dto.nome(),
                    dto.telefone(),
                    dto.senha()
            );
            EmployeeDto employee = orchestrator.updateEmployeeSaga(dtoComCodigo);
            return ResponseEntity
                    .ok(ApiResponse.success(employee));
        }catch (RuntimeException e){
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(e.getMessage(), HttpStatus.NOT_FOUND.value()));

        }
    }

    @DeleteMapping("/{codigoFuncionario}")
    public ResponseEntity<ApiResponse<EmployeeDto>> deleteEmployee(
            @PathVariable String codigoFuncionario){
        try {
            EmployeeDto employee = orchestrator.deleteEmployeeSaga(Long.parseLong(codigoFuncionario));
            return ResponseEntity
                    .ok(ApiResponse.success(employee));
        }catch (RuntimeException e){
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(e.getMessage(), HttpStatus.NOT_FOUND.value()));

        }
    }
}
