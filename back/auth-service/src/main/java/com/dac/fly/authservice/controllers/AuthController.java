package com.dac.fly.authservice.controllers;

import com.dac.fly.authservice.dto.request.LoginRequestDto;
import com.dac.fly.shared.dto.response.ApiResponse;

import com.dac.fly.authservice.dto.response.LoginResponseDto;
import com.dac.fly.authservice.dto.response.LogoutResponseDto;
import com.dac.fly.authservice.entity.Auth;
import com.dac.fly.authservice.service.AuthService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponseDto>> login(
            @RequestBody LoginRequestDto dto) {
        try {
            LoginResponseDto response = authService.login(dto.login(), dto.senha());
            return ResponseEntity.ok(ApiResponse.success(response));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.error(e.getMessage(), 401));
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<LogoutResponseDto>> logout(
            @RequestHeader("Authorization") String authHeader) {
        try {
            String token = authHeader.substring(7);
            Auth user = authService.getUserFromToken(token);
            authService.logout(token);

            return ResponseEntity.ok(ApiResponse.success(new LogoutResponseDto(user.getEmail())));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.error(e.getMessage(), 401));
        }
    }
}
