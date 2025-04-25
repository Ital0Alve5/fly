package com.dac.fly.authservice.controllers;

import com.dac.fly.authservice.dto.response.ApiResponse;
import com.dac.fly.authservice.entity.Auth;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
public class ValidationController {

    @GetMapping("/usuario")
    public ResponseEntity<ApiResponse<Auth>> usuario(Authentication authentication) {
        Auth user = (Auth) authentication.getPrincipal();
        return ResponseEntity.ok(ApiResponse.success(user));
    }

    @GetMapping("/cliente")
    public ResponseEntity<ApiResponse<Auth>> cliente(Authentication authentication) {
        Auth user = (Auth) authentication.getPrincipal();
        return ResponseEntity.ok(ApiResponse.success(user));
    }

    @GetMapping("/funcionario")
    public ResponseEntity<ApiResponse<Auth>> funcionario(Authentication authentication) {
        Auth user = (Auth) authentication.getPrincipal();
        return ResponseEntity.ok(ApiResponse.success(user));
    }
}
