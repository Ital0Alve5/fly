package com.dac.fly.reservationservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.dac.fly.shared.dto.start.StartReservationDto;

import java.util.concurrent.ConcurrentHashMap;

@Configuration
public class ReservationSagaMemoryConfig {

    @Bean
    public ConcurrentHashMap<String, StartReservationDto> store() {
        return new ConcurrentHashMap<>();
    }
}
