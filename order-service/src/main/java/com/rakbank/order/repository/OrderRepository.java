package com.rakbank.order.repository;

import com.rakbank.order.entity.OrderPurchase;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface OrderRepository extends JpaRepository<OrderPurchase, UUID> {

    Optional<OrderPurchase> findOrderPurchaseByStudentIdAndStudentGrade(String studentId, Integer studentGrade);
}
