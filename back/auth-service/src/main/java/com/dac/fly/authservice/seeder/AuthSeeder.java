package com.dac.fly.authservice.seeder;

import java.time.LocalDateTime;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.dac.fly.authservice.entity.Auth;
import com.dac.fly.authservice.repository.AuthRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class AuthSeeder implements CommandLineRunner {

    private final AuthRepository repository;

    @Override
    public void run(String... args) {
        // repository.deleteAll();

        if (repository.findByEmail("heitor@gmail.com").isEmpty()) {
            System.out.println("Inserindo usuários iniciais no MongoDB...");

            repository.save(new Auth(
                1L,
                "heitor@gmail.com",
                "03ac674216f3e15c761ee1a5e255f067953623c8b388b4459e13f978d7c846f4",
                "Heitor",
                "CLIENTE",
                LocalDateTime.now(),
                LocalDateTime.now()
            ));

            repository.save(new Auth(
                2L,
                "func_pre@gmail.com",
                "03ac674216f3e15c761ee1a5e255f067953623c8b388b4459e13f978d7c846f4",
                "Razer",
                "FUNCIONARIO",
                LocalDateTime.now(),
                LocalDateTime.now()
            ));
        }
    }
}
