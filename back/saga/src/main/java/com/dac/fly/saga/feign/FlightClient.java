package com.dac.fly.saga.feign;

import com.dac.fly.shared.dto.response.ApiResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import feign.FeignException;

import java.sql.Array;

@FeignClient(name = "flight-service", url = "http://fly-service:8080")
public interface FlightClient {

  @GetMapping("/voos/{code}/exists")
  boolean flightExists(@PathVariable("code") String code);

  @GetMapping("/voos/{code}/estado")
  ApiResponse<String> estadoByCode(@PathVariable("code") String code);

  @GetMapping("/voos/{code}/aeroportos")
  ApiResponse<String[]> aeroportosByCode(@PathVariable("code") String code);

  default String[] findAeroportosByCode(String code) {
    try {
      return aeroportosByCode(code).getData();
    } catch (FeignException.NotFound e) {
      return new String[0];
    }
  }

  default boolean existsByCode(String code) {
    try {
      return flightExists(code);
    } catch (FeignException.NotFound e) {
      return false;
    }
  }

  default String findEstadoByCode(String code) {
    try {
      return estadoByCode(code).getData();
    } catch (FeignException.NotFound e) {
      return null;
    }
  }
}
