package com.rakbank.payment.repository;

import com.rakbank.payment.entity.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserAccountRepository extends JpaRepository<UserAccount, UUID> {

    Optional<UserAccount> findUserAccountByUserId(String userId);
}
