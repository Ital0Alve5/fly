package com.dac.fly.authservice.service;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.security.SecureRandom;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.dac.fly.authservice.dto.email.EmailDto;
import com.dac.fly.authservice.dto.response.LoginResponseDto;
import com.dac.fly.shared.dto.command.CreateUserCommandDto;
import com.dac.fly.shared.dto.command.UpdateUserCommandDto;
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

    public AuthService(AuthRepository authRepository,
                       PasswordEncoder passwordEncoder,
                       JwtUtil jwtUtil,
                       EmailService emailService) {
        this.authRepository = authRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
        this.emailService = emailService;
    }

    public Auth findByEmail(String email){
        return authRepository.findByEmailAndDeletedAtIsNull(email)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado com email: " + email));
    }

    public LoginResponseDto login(String email, String senha) {
        Auth user = authRepository.findByEmailAndDeletedAtIsNull(email)
                .orElseThrow(() -> new RuntimeException("Unauthorized"));

        if (!passwordEncoder.matches(senha, user.getSenha())) {
            throw new RuntimeException("Unauthorized");
        }
        return new LoginResponseDto(
                user.getCodigoExterno(),
                jwtUtil.generateToken(user),
                "bearer",
                user.getRole()
        );
    }

    public Auth registerUser(CreateUserCommandDto dto) {
        if (authRepository.findByEmailAndDeletedAtIsNull(dto.email()).isPresent()) {
            throw new RuntimeException("Erro: email já cadastrado");
        }
        String password = dto.password() != null ? dto.password() : generateRandomPassword();
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
        Auth user = authRepository
                .findByCodigoExternoAndRoleAndDeletedAtIsNull(dto.codigoExterno(), dto.role())
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Não existe usuário com código externo: " + dto.codigoExterno() + " e role: " + dto.role()
                        )
                );

        if (dto.nome() != null && !dto.nome().isBlank()) {
            user.setNome(dto.nome());
        }
        if (dto.senha() != null && !dto.senha().isBlank()) {
            user.setSenha(passwordEncoder.encode(dto.senha()));
        }
        if (dto.email() != null && !dto.email().isBlank()) {
            user.setEmail(dto.email());
        }
        user.setAtualizadoEm(LocalDateTime.now());
        return authRepository.save(user);
    }

    public void deleteUser(String email) {
        Auth user = authRepository.findByEmailAndDeletedAtIsNull(email)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Não existe usuário cadastrado com o e-mail: " + email
                ));
        user.setDeletedAt(LocalDateTime.now());
        authRepository.save(user);
    }

    public void logout(String token) {
    }


    public Auth getUserFromToken(String token) {
        if (jwtUtil.isTokenExpired(token)) {
            throw new RuntimeException("Unauthorized");
        }
        Claims claims = jwtUtil.parseClaims(token);
        String email = claims.getSubject();
        return authRepository.findByEmailAndDeletedAtIsNull(email)
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
