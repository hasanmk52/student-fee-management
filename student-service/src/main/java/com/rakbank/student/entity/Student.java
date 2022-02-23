package com.rakbank.student.entity;

import com.rakbank.commons.constants.School;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Random;

@Entity
@Table(name = "student")
@Data
@NoArgsConstructor
public class Student {

    @Id
    @GeneratedValue
    private Long id;

    @Column(name="student_id", nullable = false, unique = true)
    private String studentId;

    @Column(name="first_name", nullable = false)
    private String firstName;

    @Column(name="last_name", nullable = false)
    private String lastName;

    @Column(name="grade", nullable = false)
    private Integer grade;

    @Column(name="mobile_number", nullable = false)
    private String mobileNumber;

    @Column(name="school_name", nullable = false)
    private String schoolName;

    @Column(name="created_date", nullable = false)
    private LocalDateTime createdDate;

    @Column(name="updated_date")
    private LocalDateTime updatedDate;

    public Student(String firstName, String lastName, Integer grade, String mobileNumber) {
        this.studentId = this.studentId != null ? this.studentId : generateStudentId();
        this.firstName = firstName;
        this.lastName = lastName;
        this.grade = grade;
        this.mobileNumber = mobileNumber;
        this.schoolName = School.SKIPLY_SCHOOL.getName();
        this.createdDate = LocalDateTime.now();
    }

    private String generateStudentId() {
        Random rnd = new Random();
        int number = rnd.nextInt(999999);
        return String.format("%06d", number);
    }
}
