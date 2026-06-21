package com.practice.lms.student.repository;

import com.practice.lms.common.model.Email;
import com.practice.lms.student.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface StudentRepository extends JpaRepository<Student, UUID> {

    boolean existsByEmail(Email email);
}
