package com.dac.fly.flyservice.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dac.fly.flyservice.entity.Aeroporto;
import com.dac.fly.flyservice.service.AirportService;
import com.dac.fly.shared.dto.response.ApiResponse;

@RestController
@RequestMapping("/aeroportos")
public class AeroportoController {

    private final AirportService airportService;

    public AeroportoController(AirportService airportService) {
        this.airportService = airportService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<Aeroporto>>> findAll() {
        return airportService.findAll();
    }
}
