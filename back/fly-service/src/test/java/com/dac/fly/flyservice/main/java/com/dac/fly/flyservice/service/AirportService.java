package com.dac.fly.flyservice.service;

import com.dac.fly.flyservice.dto.response.ApiResponse;
import com.dac.fly.flyservice.entity.Aeroporto;
import com.dac.fly.flyservice.repository.AeroportoRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

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
