package com.rakbank.payment.entity;

import com.rakbank.commons.constants.OrderType;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Random;
import java.util.UUID;

@Entity
@Table(name = "user_transaction")
@Data
@NoArgsConstructor
public class UserTransaction {

    @Id
    @Column(name = "order_id")
    private UUID orderId;

    @Column(name = "order_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private OrderType orderType;

    @Column(name = "product_code", nullable = false)
    private Integer productCode;

    @Column(name = "transaction_reference")
    private String transactionReference;

    @ManyToOne
    @JoinColumn(name = "user_account_id", nullable = false)
    private UserAccount userAccount;

    @Column(name = "amount", nullable = false)
    private BigDecimal amount;

    @Column(name = "created_date", nullable = false)
    private LocalDateTime createdDate;

    public UserTransaction(UUID orderId, Integer productCode, UserAccount userAccount, BigDecimal amount) {
        this.orderId = orderId;
        this.productCode = productCode;
        this.orderType = OrderType.TUTION_FEES;
        this.transactionReference = generateTransactionNumber();
        this.userAccount = userAccount;
        this.amount = amount;
        this.createdDate = LocalDateTime.now();
    }

    private String generateTransactionNumber() {
        int number = new Random().nextInt(999999999);
        return "RAK" + String.format("%09d", number);
    }
}
