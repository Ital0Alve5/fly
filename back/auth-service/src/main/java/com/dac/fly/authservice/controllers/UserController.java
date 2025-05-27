package com.dac.fly.authservice.controllers;

import com.dac.fly.authservice.entity.Auth;
import com.dac.fly.authservice.service.AuthService;
import com.dac.fly.shared.dto.response.ApiResponse;
import com.dac.fly.shared.dto.response.AuthDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/usuarios")
public class UserController {

    private final AuthService authService;

    public UserController(AuthService authService) {
        this.authService = authService;
    }

    @GetMapping("/{email:.+}")
    public ResponseEntity<ApiResponse<AuthDTO>> getEmployeeByEmail(@PathVariable String email) {
        try {
            Auth user = authService.findByEmail(email);
            return ResponseEntity.ok(ApiResponse.success(new com.dac.fly.shared.dto.response.AuthDTO(user.getEmail(), user.getNome(), user.getCodigoExterno(), user.getRole())));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.error(e.getMessage(), 404));
        }
    }
}
