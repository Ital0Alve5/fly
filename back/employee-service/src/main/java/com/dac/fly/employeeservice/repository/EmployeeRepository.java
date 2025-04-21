package com.dac.fly.employeeservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dac.fly.employeeservice.entity.Funcionario;

@Repository
public interface EmployeeRepository extends JpaRepository<Funcionario, Long> {
}