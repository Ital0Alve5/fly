package com.dac.fly.authservice.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.dac.fly.authservice.entity.Auth;

public interface AuthRepository extends MongoRepository<Auth, String> {
    Optional<Auth> findByEmail(String email);
}
