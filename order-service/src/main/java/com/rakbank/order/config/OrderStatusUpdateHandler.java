package com.rakbank.order.config;

import com.rakbank.commons.constants.OrderStatus;
import com.rakbank.commons.constants.PaymentStatus;
import com.rakbank.commons.dto.OrderRequestDto;
import com.rakbank.order.entity.OrderPurchase;
import com.rakbank.order.repository.OrderRepository;
import com.rakbank.order.service.OrderStatusPublisher;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;
import java.util.function.Consumer;

@Component
@AllArgsConstructor
@Slf4j
public class OrderStatusUpdateHandler {

    private final OrderRepository repository;
    private final OrderStatusPublisher publisher;

    @Transactional
    public void updateOrder(UUID id, Consumer<OrderPurchase> consumer) {
        repository.findById(id).ifPresent(consumer.andThen(this::updateOrder));
    }

    private void updateOrder(OrderPurchase purchaseOrder) {
        boolean isPaymentComplete = PaymentStatus.PAYMENT_COMPLETED.equals(purchaseOrder.getPaymentStatus());
        OrderStatus orderStatus = isPaymentComplete ? OrderStatus.ORDER_COMPLETED : OrderStatus.ORDER_CANCELLED;
        purchaseOrder.setOrderStatus(orderStatus);
        purchaseOrder.setUpdatedDate(LocalDateTime.now());
        log.info("Updated order status to {} for order-id={} with payment-status={}", orderStatus, purchaseOrder.getId(), purchaseOrder.getPaymentStatus());
        if (!isPaymentComplete) {
            publisher.publishOrderEvent(convertEntityToDto(purchaseOrder), orderStatus);
        }
    }

    public OrderRequestDto convertEntityToDto(OrderPurchase orderPurchase) {
        return new OrderRequestDto(orderPurchase.getStudentId(),
                orderPurchase.getStudentGrade(),
                orderPurchase.getAmount(),
                orderPurchase.getId().toString());
    }
}
