package com.dac.fly.authservice.seeder;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.dac.fly.authservice.entity.Auth;
import com.dac.fly.authservice.repository.AuthRepository;

@Component
public class AuthSeeder implements CommandLineRunner {

    private final AuthRepository repository;
    private final PasswordEncoder passwordEncoder;

    public AuthSeeder(AuthRepository repository, PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        if (repository.findByEmailAndDeletedAtIsNull("heitor@gmail.com").isEmpty()) {
            System.out.println("Inserindo usuários iniciais no MongoDB...");
            OffsetDateTime now = OffsetDateTime.now(ZoneOffset.of("-03:00"));
            
            repository.save(new Auth(
                    "heitor@gmail.com",
                    passwordEncoder.encode("1234"),
                    "Heitor",
                    "CLIENTE",
                    1L,
                    now,
                    now));

            repository.save(new Auth(
                    "func_pre@gmail.com",
                    passwordEncoder.encode("1234"),
                    "Razer",
                    "FUNCIONARIO",
                    1L,
                    now,
                    now));
        }
    }
}
