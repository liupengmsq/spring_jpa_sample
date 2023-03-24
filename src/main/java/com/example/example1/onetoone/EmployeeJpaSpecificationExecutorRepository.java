package com.example.example1.onetoone;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface EmployeeJpaSpecificationExecutorRepository extends JpaRepository<Employee, Long>, JpaSpecificationExecutor<Employee> {

}
