package com.dac.fly.authservice.service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import java.security.SecureRandom;

import com.dac.fly.authservice.dto.email.EmailDto;
import com.dac.fly.shared.dto.command.CreateUserCommandDto;
import com.dac.fly.shared.dto.command.UpdateUserCommandDto;
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
            System.out.println("Erro: email já cadatrado");
            return null;
        }

        String password = dto.password();
        if (dto.password() == null) {
            password = generateRandomPassword();
        }

        String encoded = passwordEncoder.encode(password);

        Auth user = new Auth(
                dto.email(),
                encoded,
                dto.nome(),
                dto.role(),
                dto.codigoExterno(),
                LocalDateTime.now(),
                LocalDateTime.now()
        );
        Auth saved = authRepository.save(user);

        try {
            emailService.sendPasswordEmail(new EmailDto(user.getNome(), user.getEmail(), password));
        } catch (Exception e) {
            System.out.println("Error sending password email " + e.getMessage());
        }


        return saved;
    }

    public Auth updateUser(UpdateUserCommandDto dto) {
        Auth user = authRepository.findByCodigoExterno(dto.codigoExterno())
                .filter(u -> u.getRole().equals(dto.role()))
                .orElseThrow(() ->
                        new IllegalArgumentException("Não existe usuário com código externo: "
                                + dto.codigoExterno() + " e role: " + dto.role())
                );

        if (dto.nome() != null && !dto.nome().isBlank()) {
            user.setNome(dto.nome());
        }

        if (dto.senha() != null && !dto.senha().isBlank()) {
            String encoded = passwordEncoder.encode(dto.senha());
            user.setSenha(encoded);
        }

        if (dto.email() != null && !dto.email().isBlank()) {
            user.setEmail(dto.email());
        }

        user.setAtualizadoEm(LocalDateTime.now());

        return authRepository.save(user);
    }
    public void deleteUser(String email) {
        Optional<Auth> optionalUser = authRepository.findByEmail(email);

        if (optionalUser.isPresent()) {
            Auth user = optionalUser.get();
            authRepository.delete(user);
            System.out.println("Usuário com e-mail " + email + " removido com sucesso.");
        } else {
            throw new IllegalArgumentException("Não existe usuário cadastrado com o e-mail: " + email);
        }
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
