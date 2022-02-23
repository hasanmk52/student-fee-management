package com.rakbank.commons.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentReceiptDto {

    private String studentId;
    private String studentName;
    private String schoolName;
    private Integer studentGrade;
    private String transactionTimeStr;
    private String transactionReference;
    private String paymentCardType;
    private String paymentCardNumber;
    private String orderType;
    private BigDecimal orderAmount;

}
