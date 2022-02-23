package com.rakbank.commons.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderRequestDto {

    @NotBlank
    private String studentId;
    @NotNull
    private Integer studentGrade;
    @NotNull
    private BigDecimal amount;
    private String orderId;
}
