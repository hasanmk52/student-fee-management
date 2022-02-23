package com.rakbank.student.service;

import com.rakbank.commons.dto.StudentDto;
import com.rakbank.commons.exception.ErrorCode;
import com.rakbank.commons.exception.StudentFeeManagementException;
import com.rakbank.student.entity.Student;
import com.rakbank.student.repository.StudentRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@AllArgsConstructor
public class StudentService {

    private StudentRepository studentRepository;

    @Transactional
    public Student createStudent(StudentDto studentDto) {
        validateStudent(studentDto);
        return studentRepository.save(convertDtoToEntity(studentDto));
    }

    @Transactional
    public Student updateStudentGrade(String studentId, Integer grade) {
        Optional<Student> studentOptional = find(studentId);
        if(studentOptional.isPresent()) {
            Student student = studentOptional.get();
            student.setGrade(grade);
            student.setUpdatedDate(LocalDateTime.now());
            return student;
        } else {
            throw new StudentFeeManagementException(ErrorCode.BAD_DATA, "Update failed as cannot find Student with studentId=%s", studentId);
        }
    }

    @Transactional
    public void deleteStudent(String studentId) {
        Optional<Student> studentOptional = find(studentId);
        if(studentOptional.isPresent()) {
            studentRepository.delete(studentOptional.get());
        } else {
            throw new StudentFeeManagementException(ErrorCode.BAD_DATA, "Delete failed as cannot find Student with studentId=%s", studentId);
        }
    }

    public Optional<Student> find(String studentId) {
        return studentRepository.findStudentByStudentId(studentId);
    }

    public List<Student> getAllStudents(){
        return studentRepository.findAll();
    }

    //Initialize with some student data
    @PostConstruct
    public void initStudentsInDB() {
        studentRepository.saveAll(Stream.of(new Student("Hasan", "Kagalwala", 9, "+971523985572"),
                                new Student("Sharafudeen", "Aboobacker", 10, "+971545465657"))
                .collect(Collectors.toList()));
    }

    private Student convertDtoToEntity(StudentDto dto) {
        return new Student(dto.getFirstName(),
                dto.getLastName(),
                dto.getGrade(),
                dto.getMobileNumber());
    }

    private void validateStudent(StudentDto dto) {
        studentRepository.findStudentByFirstNameAndLastName(dto.getFirstName(), dto.getLastName())
                .ifPresent(op -> {
                    throw new StudentFeeManagementException(ErrorCode.BAD_DATA, "A student with this name already exists.");
                });
    }
}
