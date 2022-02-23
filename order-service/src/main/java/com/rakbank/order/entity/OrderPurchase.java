package com.rakbank.order.entity;

import com.rakbank.commons.constants.OrderType;
import com.rakbank.commons.constants.OrderStatus;
import com.rakbank.commons.constants.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "order_purchase")
@Data
@NoArgsConstructor
public class OrderPurchase {

    @Id
    private UUID id;

    @Column(name="student_id", nullable = false)
    private String studentId;

    @Column(name="student_grade", nullable = false)
    private Integer studentGrade;

    @Column(name="order_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private OrderType orderType;

    @Column(name="amount", nullable = false)
    private BigDecimal amount;

    @Column(name="order_status", nullable = false)
    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    @Column(name="payment_status", nullable = false)
    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus;

    @Column(name="created_date", nullable = false)
    private LocalDateTime createdDate;

    @Column(name="updated_date")
    private LocalDateTime updatedDate;

    public OrderPurchase(String studentId, Integer studentGrade, BigDecimal amount, OrderStatus orderStatus, PaymentStatus paymentStatus) {
        this.studentId = studentId;
        this.studentGrade = studentGrade;
        this.orderType = OrderType.TUTION_FEES;
        this.amount = amount;
        this.orderStatus = orderStatus;
        this.paymentStatus = paymentStatus;
        this.createdDate = LocalDateTime.now();
    }
}
