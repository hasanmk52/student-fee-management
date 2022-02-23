package com.rakbank.payment.service;

import com.rakbank.payment.entity.UserTransaction;
import com.rakbank.payment.repository.UserTransactionRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class UserTransactionService {

    private final UserTransactionRepository userTransactionRepository;

    public Optional<UserTransaction> findByOrderId(String orderId) {
        return userTransactionRepository.findById(UUID.fromString(orderId));
    }
}
