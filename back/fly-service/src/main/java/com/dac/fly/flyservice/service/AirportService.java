package com.dac.fly.flyservice.service;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.dac.fly.flyservice.entity.Aeroporto;
import com.dac.fly.flyservice.repository.AeroportoRepository;
import com.dac.fly.shared.dto.response.ApiResponse;


@Service
public class AirportService {

    private final AeroportoRepository aeroportoRepository;

    public AirportService(AeroportoRepository aeroportoRepository) {
        this.aeroportoRepository = aeroportoRepository;
    }

    public ResponseEntity<ApiResponse<List<Aeroporto>>> findAll() {
        List<Aeroporto> aeroportos = aeroportoRepository.findAll();
        return ResponseEntity.ok(ApiResponse.success(aeroportos));
    }
}
