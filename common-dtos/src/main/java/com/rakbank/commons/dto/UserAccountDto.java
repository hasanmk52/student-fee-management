package com.rakbank.commons.dto;

import com.rakbank.commons.constants.CardType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserAccountDto {

    private String accountId;
    @NotBlank
    private String userId;
    @NotBlank
    private String cardNumber;
    @NotNull
    private CardType cardType;
    @NotNull
    private BigDecimal balance;
}
