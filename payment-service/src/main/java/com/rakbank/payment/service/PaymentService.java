package com.rakbank.payment.service;

import com.rakbank.commons.constants.PaymentStatus;
import com.rakbank.commons.dto.OrderRequestDto;
import com.rakbank.commons.dto.PaymentRequestDto;
import com.rakbank.commons.event.OrderEvent;
import com.rakbank.commons.event.PaymentEvent;
import com.rakbank.payment.entity.UserTransaction;
import com.rakbank.payment.repository.UserAccountRepository;
import com.rakbank.payment.repository.UserTransactionRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@AllArgsConstructor
@Slf4j
public class PaymentService {

    private final UserAccountRepository userAccountRepository;
    private final UserTransactionRepository userTransactionRepository;

    /*
     * Get the User Id and check for balance availability
     * If balance is sufficient -> Payment completed and deduct amount from DB
     * If payment is insufficient -> Cancel order event and update the amount in DB
     * */
    @Transactional
    public PaymentEvent newOrderEvent(final OrderEvent orderEvent) {
        OrderRequestDto orderRequestDto = orderEvent.getOrderRequestDto();
        log.info("Creating payment event for order={}", orderRequestDto);
        PaymentRequestDto paymentRequestDto = new PaymentRequestDto(orderRequestDto.getOrderId(),
                orderRequestDto.getStudentId(),
                orderRequestDto.getAmount());

        PaymentEvent paymentEvent = userAccountRepository.findUserAccountByUserId(orderRequestDto.getStudentId())
                .filter(sb -> sb.getBalance().compareTo(orderRequestDto.getAmount()) > 0)
                .map(sb -> {
                    sb.setBalance(sb.getBalance().subtract(orderRequestDto.getAmount()));
                    userTransactionRepository.save(new UserTransaction(UUID.fromString(orderRequestDto.getOrderId()),
                            orderRequestDto.getStudentGrade(),
                            sb,
                            orderRequestDto.getAmount()));
                    return new PaymentEvent(paymentRequestDto, PaymentStatus.PAYMENT_COMPLETED);
                })
                .orElse(new PaymentEvent(paymentRequestDto, PaymentStatus.PAYMENT_FAILED));
        log.info("Created PaymentEvent={}", paymentEvent);
        return paymentEvent;
    }

    @Transactional
    public void cancelOrderEvent(final OrderEvent orderEvent) {
        userTransactionRepository.findById(UUID.fromString(orderEvent.getOrderRequestDto().getOrderId()))
                .ifPresent( st -> {
                    userTransactionRepository.delete(st);
                    userAccountRepository.findUserAccountByUserId(st.getUserAccount().getUserId())
                            .ifPresent(sa -> sa.setBalance(sa.getBalance().add(st.getAmount())));
                });
        log.info("Cancelled for order={} due to insufficient account balance", orderEvent.getOrderRequestDto().getOrderId());
    }
}
