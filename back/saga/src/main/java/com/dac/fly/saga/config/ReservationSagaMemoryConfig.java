package com.dac.fly.saga.config;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.dac.fly.shared.dto.events.MilesUpdatedEvent;
import com.dac.fly.shared.dto.events.SeatsUpdatedEvent;
import com.dac.fly.shared.dto.response.CanceledReservationResponseDto;
import com.dac.fly.shared.dto.response.CreatedReservationResponseDto;

@Configuration
public class ReservationSagaMemoryConfig {

    @Bean
    public ConcurrentHashMap<String, CompletableFuture<MilesUpdatedEvent>> milesResponses() {
        return new ConcurrentHashMap<>();
    }

    @Bean
    public ConcurrentHashMap<String, CompletableFuture<SeatsUpdatedEvent>> seatsResponses() {
        return new ConcurrentHashMap<>();
    }

    @Bean
    public ConcurrentHashMap<String, CompletableFuture<CreatedReservationResponseDto>> reservationResponses() {
        return new ConcurrentHashMap<>();
    }

    @Bean
    public ConcurrentHashMap<String, CompletableFuture<CanceledReservationResponseDto>> reservationCancelResponses() {
        return new ConcurrentHashMap<>();
    }
}
