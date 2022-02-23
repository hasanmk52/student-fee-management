package com.rakbank.student.repository;

import com.rakbank.student.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StudentRepository extends JpaRepository<Student, Long> {

    Optional<Student> findStudentByStudentId(String studentId);
    Optional<Student> findStudentByFirstNameAndLastName(String firstName, String lastName);
}
