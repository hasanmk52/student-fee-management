package com.rakbank.commons.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StudentDto {

    private String studentId;
    @NotBlank
    private String firstName;
    @NotBlank
    private String lastName;
    @NotNull
    private Integer grade;
    @NotBlank
    private String mobileNumber;
    private String schoolName;

    public String getFullName() {
        return firstName + " " + lastName;
    }
}
