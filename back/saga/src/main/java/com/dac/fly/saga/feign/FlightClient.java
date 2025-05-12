package com.dac.fly.saga.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import feign.FeignException;

@FeignClient(name = "flight-service", url = "http://fly-service:8080")
public interface FlightClient {

  @GetMapping("/voos/{code}/exists")
  boolean flightExists(@PathVariable("code") String code);

  default boolean existsByCode(String code) {
    try {
      return flightExists(code);
    } catch (FeignException.NotFound e) {
      return false;
    }
  }
}
