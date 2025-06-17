package com.dac.fly.flyservice.controllers;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dac.fly.flyservice.dto.request.CreateNewFlightRequestDto;
import com.dac.fly.flyservice.dto.response.FlightDetailsResponseDto;
import com.dac.fly.flyservice.dto.response.FlightResponseDto;
import com.dac.fly.flyservice.service.FlightService;
import com.dac.fly.shared.dto.response.ApiResponse;

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
            inicio = data.atStartOfDay().atOffset(ZoneOffset.of("-03:00"));
        }

        if (dataFim != null) {
            fim = dataFim.plusDays(1).atStartOfDay().atOffset(ZoneOffset.of("-03:00"));
        }

        if (origem != null || destino != null) {
            return flightService.findByAirport(inicio, origem, destino);
        }

        List<FlightDetailsResponseDto> voos = flightService.findAll(inicio, fim, origem, destino);

        Map<String, Object> resp = new LinkedHashMap<>();
        resp.put("inicio", data != null ? data.toString() : null);
        resp.put("fim", dataFim != null ? dataFim.toString() : null);
        resp.put("voos", voos);

        return ResponseEntity.ok(ApiResponse.success(resp));
    }

    @GetMapping("/{codigo}")
    public ResponseEntity<ApiResponse<FlightDetailsResponseDto>> findByCode(@PathVariable String codigo) {
        return flightService.findByCode(codigo);
    }

    @GetMapping("/{codigo}/aeroportos")
    public ResponseEntity<ApiResponse<String[]>> findAeroportosByCode(@PathVariable String codigo) {
        ResponseEntity<ApiResponse<FlightDetailsResponseDto>> flight = flightService.findByCode(codigo);

        String[] aeroportos = new String[]{
                flight.getBody().getData().aeroporto_origem().codigo(),
                flight.getBody().getData().aeroporto_destino().codigo()
        };

        return ResponseEntity.ok(ApiResponse.success(aeroportos));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<FlightResponseDto>> create(@RequestBody CreateNewFlightRequestDto dto) {
        return flightService.create(dto);
    }

    @GetMapping("/{codigo}/exists")
    public ResponseEntity<Boolean> existsByCode(@PathVariable("codigo") String codigo) {
        boolean exists = flightService.existsByCodigo(codigo);
        return ResponseEntity.ok(exists);
    }

    @GetMapping("/{codigo}/estado")
    public ResponseEntity<ApiResponse<String>> findEstadoByCode(@PathVariable("codigo") String codigo) {
        return flightService.findEstadoByCode(codigo);
    }

}
