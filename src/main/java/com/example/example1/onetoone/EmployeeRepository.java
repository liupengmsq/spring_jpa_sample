package com.example.example1.onetoone;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    List<Employee> findAllByFirstNameContains(String firstNameContains);
}
