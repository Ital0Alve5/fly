package com.dac.fly.flyservice.controllers;

import com.dac.fly.flyservice.dto.request.CreateNewFlightRequestDto;
import com.dac.fly.flyservice.dto.request.UpdateFlightStatusRequestDto;
import com.dac.fly.flyservice.dto.response.ApiResponse;
import com.dac.fly.flyservice.dto.response.FlightDetailsResponseDto;
import com.dac.fly.flyservice.dto.response.FlightResponseDto;
import com.dac.fly.flyservice.service.FlightService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

@RestController
@RequestMapping({ "/voos", "/voo" })
public class VooController {

    private final FlightService flightService;

    public VooController(FlightService flightService) {
        this.flightService = flightService;
    }

    @GetMapping
    public ResponseEntity<?> findAll(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate data,
            @RequestParam(required = false, name = "data-fim") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataFim,
            @RequestParam(required = false) String origem,
            @RequestParam(required = false) String destino) {

        OffsetDateTime inicio = null;
        OffsetDateTime fim = null;

        if (data != null) {
            inicio = data.atStartOfDay().atOffset(ZoneOffset.UTC);
        }

        if (dataFim != null) {
            fim = dataFim.plusDays(1).atStartOfDay().atOffset(ZoneOffset.UTC);
        }

        if (inicio != null && origem != null && destino != null) {
            return flightService.findByAirport(inicio, origem, destino);
        }

        return flightService.findAll(inicio, fim, origem, destino);
    }

    @GetMapping("/{codigo}")
    public ResponseEntity<ApiResponse<FlightDetailsResponseDto>> findByCode(@PathVariable String codigo) {
        return flightService.findByCode(codigo);
    }

    @PostMapping
    public ResponseEntity<ApiResponse<FlightResponseDto>> create(@RequestBody CreateNewFlightRequestDto dto) {
        return flightService.create(dto);
    }

    @PatchMapping("/{codigo}/estado")
    public ResponseEntity<ApiResponse<FlightResponseDto>> updateStatus(@PathVariable String codigo,
            @RequestBody UpdateFlightStatusRequestDto estadoDto) {
        return flightService.updateStatus(codigo, estadoDto);
    }
}
