package com.rakbank.commons.dto;

import com.rakbank.commons.constants.OrderStatus;
import com.rakbank.commons.constants.OrderType;
import com.rakbank.commons.constants.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderPurchaseDto {

    private String orderId;
    private String studentId;
    private Integer studentGrade;
    private OrderType orderType;
    private BigDecimal amount;
    private OrderStatus orderStatus;
    private PaymentStatus paymentStatus;
    private LocalDateTime createdDate;
}