package com.dac.fly.employeeservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dac.fly.employeeservice.entity.Funcionario;

import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<Funcionario, Long> {
    Optional<Funcionario> findByCpf(String cpf);
    Optional<Funcionario> findByCodigo(Long codigo);
    Optional<Funcionario> findByEmail(String email);
}