package com.campuscrave.api.repository;

import com.campuscrave.api.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StudentRepository extends JpaRepository<Student, Long> {

    Optional<Student> findByRollNumber(String rollNumber);
}
