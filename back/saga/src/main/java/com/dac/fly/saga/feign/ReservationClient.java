package com.dac.fly.saga.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import feign.FeignException;

@FeignClient(name = "reservation-service", url = "http://reservation-service:8080")
public interface ReservationClient {

    @GetMapping("/reservas/{code}/exists")
    boolean reservationExists(@PathVariable("code") String code);

    default boolean existsByCode(String code) {
        try {
            return reservationExists(code);
        } catch (FeignException.NotFound e) {
            return false;
        }
    }
}
