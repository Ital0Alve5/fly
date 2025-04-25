package com.dac.fly.authservice.seeder;

import java.time.LocalDateTime;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.dac.fly.authservice.entity.Auth;
import com.dac.fly.authservice.repository.AuthRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class AuthSeeder implements CommandLineRunner {

    private final AuthRepository repository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        // repository.deleteAll();

        if (repository.findByEmail("heitor@gmail.com").isEmpty()) {
            System.out.println("Inserindo usuários iniciais no MongoDB...");

            repository.save(new Auth(
                    1L,
                    "heitor@gmail.com",
                    passwordEncoder.encode("123456"),
                    "Heitor",
                    "CLIENTE",
                    LocalDateTime.now(),
                    LocalDateTime.now()
            ));

            repository.save(new Auth(
                    2L,
                    "func_pre@gmail.com",
                    passwordEncoder.encode("123456"),
                    "Razer",
                    "FUNCIONARIO",
                    LocalDateTime.now(),
                    LocalDateTime.now()
            ));
        }
    }
}
