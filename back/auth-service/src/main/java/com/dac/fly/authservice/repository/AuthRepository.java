package com.dac.fly.authservice.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.dac.fly.authservice.entity.Auth;

@Repository
public interface AuthRepository extends MongoRepository<Auth, String> {
    Optional<Auth> findByEmailAndDeletedAtIsNull(String email);
    Optional<Auth> findByCodigoExternoAndRoleAndDeletedAtIsNull(Long codigoExterno, String role);
}
