package com.dac.fly.flyservice.controllers;

import com.dac.fly.flyservice.dto.response.ApiResponse;
import com.dac.fly.flyservice.entity.Aeroporto;
import com.dac.fly.flyservice.service.AirportService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
