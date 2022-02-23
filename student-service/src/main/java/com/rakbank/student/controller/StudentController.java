package com.rakbank.student.controller;

import com.rakbank.commons.dto.PaymentReceiptDto;
import com.rakbank.commons.dto.StudentDto;
import com.rakbank.commons.exception.ErrorCode;
import com.rakbank.commons.exception.StudentFeeManagementException;
import com.rakbank.student.entity.Student;
import com.rakbank.student.service.StudentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/student")
@AllArgsConstructor
public class StudentController {

    private final StudentService studentService;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public List<StudentDto> getStudents(){
        return studentService.getAllStudents().stream().map(this::convertEntityToDto).collect(Collectors.toList());
    }

    @Operation(summary = "Get Student", description = "Get student by student Id", tags = "Get")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Student found",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = StudentDto.class))}),
            @ApiResponse(responseCode = "404", description = "Student not found", content = @Content)})
    @GetMapping(value = "/{studentId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public StudentDto getStudent(
            @Parameter(description = "Student Id for the student you need to retrieve (6-digit)", example = "343545", required = true) @PathVariable String studentId) {
        Optional<Student> studentOptional = studentService.find(studentId);
        if(!studentOptional.isPresent()) {
            throw new StudentFeeManagementException(ErrorCode.BAD_DATA, "Cannot find Student with studentId = {}", studentId);
        }
        return convertEntityToDto(studentOptional.get());
    }

    @Operation(summary = "Add/Create Student", description = "Creates a new student", tags = "Create")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Student Created",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = StudentDto.class))}),
            @ApiResponse(responseCode = "400", description = "Student bad data", content = @Content)})
    @PostMapping(value = "/create", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<StudentDto> createStudent(@Valid @RequestBody StudentDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(convertEntityToDto(studentService.createStudent(dto)));
    }

    @PatchMapping("/updateGrade/{studentId}/{grade}")
    public ResponseEntity<StudentDto> update(@PathVariable String studentId, @PathVariable Integer grade) {
        return ResponseEntity.status(HttpStatus.OK).body(convertEntityToDto(studentService.updateStudentGrade(studentId, grade)));
    }

    @DeleteMapping(value = "/delete/{studentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable final String studentId) {
        studentService.deleteStudent(studentId);
    }

    private StudentDto convertEntityToDto(Student entity) {
        return new StudentDto(entity.getStudentId(),
                entity.getFirstName(),
                entity.getLastName(),
                entity.getGrade(),
                entity.getMobileNumber(),
                entity.getSchoolName());
    }

}
