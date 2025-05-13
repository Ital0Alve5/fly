package com.dac.fly.saga.feign;

import com.dac.fly.shared.dto.response.ApiResponse;
import com.dac.fly.shared.dto.response.EmployeeDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import feign.FeignException;

@FeignClient(name = "client-service", url = "http://client-service:8080")
public interface ClientClient {

  @GetMapping("/clientes/{code}/exists")
  boolean clientExists(@PathVariable("code") Long code);

  @GetMapping("/clientes/{cpf}/exists/cpf")
  boolean clientExistsByCpf(@PathVariable("cpf") String cpf);

  default boolean existsByCode(Long code) {
    try {
      return clientExists(code);
    } catch (FeignException.NotFound e) {
      return false;
    }
  }

  default boolean existsByCpf(String cpf) {
    try {
      return clientExistsByCpf(cpf);
    } catch (FeignException e) {
      return false;
    }
  }
}
