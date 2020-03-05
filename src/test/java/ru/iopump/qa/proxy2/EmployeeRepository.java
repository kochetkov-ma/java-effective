package ru.iopump.qa.proxy2;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeRepository extends JpaRepository<SpringTest.Employee, Long> {
    SpringTest.Employee findByName(String name);
}