package com.dac.fly.saga.config;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.dac.fly.shared.dto.events.FlightReservationsCancelledEventDto;
import com.dac.fly.shared.dto.events.MilesUpdatedEvent;
import com.dac.fly.shared.dto.events.SeatsUpdatedEvent;
import com.dac.fly.shared.dto.response.CanceledReservationResponseDto;
import com.dac.fly.shared.dto.response.CancelledFlightResponseDto;
import com.dac.fly.shared.dto.response.CreatedReservationResponseDto;

/**
 * Configuração única de memória para todos os fluxos de Saga
 */
@Configuration
public class SagaMemoryConfig {

    /**
     * Futuro para a resposta de criação de reserva na Saga.
     */
    @Bean
    public ConcurrentHashMap<String, CompletableFuture<CreatedReservationResponseDto>> reservationResponses() {
        return new ConcurrentHashMap<>();
    }

    /**
     * Futuro para a resposta de cancelamento de reserva na Saga.
     */
    @Bean
    public ConcurrentHashMap<String, CompletableFuture<CanceledReservationResponseDto>> reservationCancelResponses() {
        return new ConcurrentHashMap<>();
    }

    /**
     * Futuro para a resposta de devolução/dedução de milhas pelo client-service.
     */
    @Bean
    public ConcurrentHashMap<String, CompletableFuture<MilesUpdatedEvent>> milesResponses() {
        return new ConcurrentHashMap<>();
    }

    /**
     * Futuro para a resposta de reserva de assentos pelo fly-service.
     */
    @Bean
    public ConcurrentHashMap<String, CompletableFuture<SeatsUpdatedEvent>> seatsResponses() {
        return new ConcurrentHashMap<>();
    }

    /**
     * Futuro para a resposta de cancelamento de voo pelo fly-service.
     */
    @Bean
    public ConcurrentHashMap<String, CompletableFuture<CancelledFlightResponseDto>> flightCancelResponses() {
        return new ConcurrentHashMap<>();
    }

    /**
     * Futuro para o evento de reservas canceladas após um voo ser cancelado.
     */
    @Bean
    public ConcurrentHashMap<String, CompletableFuture<FlightReservationsCancelledEventDto>> reservationsCancelResponses() {
        return new ConcurrentHashMap<>();
    }
}
