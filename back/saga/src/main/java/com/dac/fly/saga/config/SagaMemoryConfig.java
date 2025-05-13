package com.dac.fly.saga.config;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

import com.dac.fly.shared.dto.events.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

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
    public ConcurrentHashMap<String, CompletableFuture<CancelledReservationEventDto>> reservationCancelResponses() {
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
    public ConcurrentHashMap<String, CompletableFuture<CancelledFlightEventDto>> flightCancelResponses() {
        return new ConcurrentHashMap<>();
    }

    /**
     * Futuro para o evento de reservas canceladas após um voo ser cancelado.
     */
    @Bean
    public ConcurrentHashMap<String, CompletableFuture<FlightReservationsCancelledEventDto>> reservationsCancelResponses() {
        return new ConcurrentHashMap<>();
    }

    /**
     * Futuro para o evento de criar cliente para a resposta de criar cliente na saga.
     */
    @Bean
    ConcurrentHashMap<String, CompletableFuture<ClientCreatedEventDto>> clientCreateResponses() {
        return new ConcurrentHashMap<>();
    }

    /**
     * Futuro para o evento de criar usuário para a resposta de criar usuário no auth-service.
     */
    @Bean
    ConcurrentHashMap<String, CompletableFuture<UserCreatedEventDto>> userCreateResponses() {
        return new ConcurrentHashMap<>();
    }

    /**
     * Futuro para o evento de criar funcionario para a resposta de criar funcionario na saga.
     */
    @Bean
    ConcurrentHashMap<String, CompletableFuture<EmployeeCreatedEventDto>> employeeCreateResponses() {
        return new ConcurrentHashMap<>();
    }

    /**
     * Futuro para o evento de atualizar funcionário para a resposta de criar funcionário na saga.
     */
    @Bean
    ConcurrentHashMap<String, CompletableFuture<EmployeeUpdatedEventDto>> employeeUpdateResponses() {
        return new ConcurrentHashMap<>();
    }

    /**
     * Futuro para o evento de deletar funcionário para a resposta de criar funcionário na saga.
     */
    @Bean
    ConcurrentHashMap<String, CompletableFuture<EmployeeDeletedEventDto>> employeeDeleteResponses() {
        return new ConcurrentHashMap<>();
    }

    /**
     * Futuro para o evento de atualizar usuário para a resposta de atualizar usuário no auth-service.
     */
    @Bean
    ConcurrentHashMap<String, CompletableFuture<UserUpdatedEventDto>> userUpdateResponses() {
        return new ConcurrentHashMap<>();
    }

    /**
     * Futuro para o evento de deletar usuário para a resposta de deletar usuário no auth-service.
     */
    @Bean
    ConcurrentHashMap<String, CompletableFuture<UserDeletedEventDto>> userDeleteResponses() {
        return new ConcurrentHashMap<>();
    }
}
