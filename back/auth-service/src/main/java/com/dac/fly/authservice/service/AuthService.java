package com.dac.fly.authservice.service;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.dac.fly.authservice.entity.Auth;
import com.dac.fly.authservice.repository.AuthRepository;
import com.dac.fly.authservice.util.JwtUtil;

import io.jsonwebtoken.Claims;

@Service
public class AuthService {

    private final AuthRepository authRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final Set<String> tokenBlacklist = ConcurrentHashMap.newKeySet();

    public AuthService(AuthRepository authRepository,
                       PasswordEncoder passwordEncoder,
                       JwtUtil jwtUtil) {
        this.authRepository = authRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    public String login(String email, String senha) {
        Auth user = authRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Unauthorized"));

        if (!passwordEncoder.matches(senha, user.getSenha())) {
            throw new RuntimeException("Unauthorized");
        }
        return jwtUtil.generateToken(user);
    }

    public void logout(String token) {
        tokenBlacklist.add(token);
    }

    public boolean isBlacklisted(String token) {
        return tokenBlacklist.contains(token);
    }

    public Auth getUserFromToken(String token) {
        if (isBlacklisted(token) || jwtUtil.isTokenExpired(token)) {
            throw new RuntimeException("Unauthorized");
        }

        Claims claims = jwtUtil.parseClaims(token);
        String email = claims.getSubject();
        return authRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Unauthorized"));
    }

}
