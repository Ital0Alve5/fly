package com.dac.fly.clientservice.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dac.fly.clientservice.entity.Client;

@Repository
public interface ClientRepository extends JpaRepository<Client, Integer> {
    boolean existsByCpf(String cpf);
    boolean existsByEmail(String email);
    Optional<Client> findByCpf(String cpf);
    Optional<Client> findByCodigo(Long codigo);
}
