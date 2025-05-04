package com.dac.fly.authservice.service;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import java.security.SecureRandom;

import com.dac.fly.authservice.dto.email.EmailDto;
import com.dac.fly.shared.dto.command.CreateUserCommandDto;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.dac.fly.authservice.entity.Auth;
import com.dac.fly.authservice.repository.AuthRepository;
import com.dac.fly.authservice.util.JwtUtil;

import io.jsonwebtoken.Claims;

@Service
public class AuthService {

    private static final SecureRandom RANDOM = new SecureRandom();
    private final AuthRepository authRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final EmailService emailService;
    private final Set<String> tokenBlacklist = ConcurrentHashMap.newKeySet();

    public AuthService(AuthRepository authRepository,
                       PasswordEncoder passwordEncoder,
                       JwtUtil jwtUtil,
                       EmailService emailService) {
        this.authRepository = authRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
        this.emailService = emailService;
    }

    public String login(String email, String senha) {
        Auth user = authRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Unauthorized"));

        if (!passwordEncoder.matches(senha, user.getSenha())) {
            throw new RuntimeException("Unauthorized");
        }
        return jwtUtil.generateToken(user);
    }

    public Auth registerUser(CreateUserCommandDto dto) {
        if (authRepository.findByEmail(dto.email()).isPresent()) {
            throw new IllegalArgumentException("E-mail já cadastrado.");
        }

        String rawPassword = generateRandomPassword();
        String encoded = passwordEncoder.encode(rawPassword);

        Auth user = new Auth(
                dto.email(),
                encoded,
                dto.nome(),
                dto.role(),
                LocalDateTime.now(),
                LocalDateTime.now()
        );
        Auth saved = authRepository.save(user);

        try {
            emailService.sendPasswordEmail(new EmailDto(user.getNome(), user.getEmail(), rawPassword));
        }catch (Exception e) {
            System.out.println("Error sending password email " + e.getMessage());
        }

        return saved;
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


    private String generateRandomPassword() {
        StringBuilder password = new StringBuilder(6);
        for (int i = 0; i < 6; i++) {
            password.append(RANDOM.nextInt(10));
        }
        return password.toString();
    }
}
