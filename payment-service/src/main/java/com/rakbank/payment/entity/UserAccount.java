package com.rakbank.payment.entity;

import com.rakbank.commons.constants.CardType;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "user_account")
@Data
@NoArgsConstructor
public class UserAccount {

    @Id
    private UUID id;

    @Column(name = "user_id", nullable = false, unique = true)
    private String userId;

    @Column(name = "user_name", nullable = false)
    private String userName;

    @Column(name = "card_number", nullable = false, unique = true)
    private String cardNumber;

    @Column(name = "card_type", nullable = false, unique = true)
    @Enumerated(EnumType.STRING)
    private CardType cardType;

    @Column(name = "balance", nullable = false)
    private BigDecimal balance;

    @Column(name = "created_date", nullable = false)
    private LocalDateTime createdDate;

    public UserAccount(String userId, String userName, String cardNumber, CardType cardType, BigDecimal balance) {
        this.id = UUID.randomUUID();
        this.userId = userId;
        this.userName = userName;
        this.cardNumber = cardNumber;
        this.cardType = cardType;
        this.balance = balance;
        this.createdDate = LocalDateTime.now();
    }
}
