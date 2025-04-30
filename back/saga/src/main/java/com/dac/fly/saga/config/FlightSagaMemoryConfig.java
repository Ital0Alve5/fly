package com.dac.fly.saga.config;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.dac.fly.shared.dto.events.FlightReservationsCancelledEventDto;
import com.dac.fly.shared.dto.events.MilesUpdatedEvent;
import com.dac.fly.shared.dto.response.CancelledFlightResponseDto;

@Configuration
public class FlightSagaMemoryConfig {
    @Bean
    public ConcurrentHashMap<String, CompletableFuture<CancelledFlightResponseDto>> flightCancelResponses() {
        return new ConcurrentHashMap<>();
    }

    @Bean
    public ConcurrentHashMap<String, CompletableFuture<FlightReservationsCancelledEventDto>> flightReservationsCancelledResponses() {
        return new ConcurrentHashMap<>();
    }

    @Bean
    public ConcurrentHashMap<String, CompletableFuture<MilesUpdatedEvent>> milesResponses() {
        return new ConcurrentHashMap<>();
    }
}
