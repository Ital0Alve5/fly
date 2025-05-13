package com.dac.fly.saga.feign;

import com.dac.fly.shared.dto.response.ApiResponse;
import com.dac.fly.shared.dto.response.EmployeeDto;
import feign.FeignException;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "employee-service", url = "http://employee-service:8080")
public interface EmployeeClient {

    @GetMapping("/funcionarios/{code}")
    ApiResponse<EmployeeDto> findByCode(@PathVariable("code") Long code);

    @GetMapping("/funcionarios/{email}/email")
    ApiResponse<EmployeeDto> findByEmail(@PathVariable("email") String email);

    @GetMapping("/funcionarios/{cpf}/cpf")
    ApiResponse<EmployeeDto> findByCpf(@PathVariable("cpf") String cpf);

    default EmployeeDto findEmployeeByCpf(String cpf) {
        try {
            return findByCpf(cpf).getData();
        } catch (FeignException e) {
            return null;
        }
    }

    default EmployeeDto findEmployeeByCode(Long code) {
        try {
            return findByCode(code).getData();
        } catch (FeignException.NotFound e) {
            return null;
        }
    }

    default EmployeeDto findEmployeeByEmail(String email) {
        try {
            System.out.println("Fething by email");
            return findByEmail(email).getData();
        } catch (FeignException e) {
            return null;
        }
    }
}
