package com.rakbank.payment.service;

import com.rakbank.commons.constants.OrderStatus;
import com.rakbank.commons.constants.School;
import com.rakbank.commons.dto.OrderPurchaseDto;
import com.rakbank.commons.dto.PaymentReceiptDto;
import com.rakbank.commons.exception.ErrorCode;
import com.rakbank.commons.exception.StudentFeeManagementException;
import com.rakbank.payment.entity.UserAccount;
import com.rakbank.payment.entity.UserTransaction;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
@AllArgsConstructor
@Slf4j
public class PaymentReceiptService {

    private static final String ORDER_URL = "http://localhost:8082/order-service/order";

    private final RestTemplate restTemplate;
    private final UserTransactionService userTransactionService;

    public PaymentReceiptDto fetchPaymentReceipt(final String orderId) {
        //Validate if the order is a valid one
        OrderPurchaseDto orderPurchaseDto = getOrder(orderId);
        if(orderPurchaseDto == null) {
            throw new StudentFeeManagementException(ErrorCode.BAD_DATA, "Cannot fetch receipt info as orderId = %s is invalid", orderId);
        }
        if(orderPurchaseDto.getOrderStatus().equals(OrderStatus.ORDER_CANCELLED)) {
            throw new StudentFeeManagementException(ErrorCode.BAD_DATA, "Cannot fetch receipt info for a order that was failed.");
        }
        UserTransaction userTransaction = userTransactionService
                .findByOrderId(orderId)
                .orElseThrow(() -> new StudentFeeManagementException(ErrorCode.BAD_DATA, "No payment transaction found for orderId = %s", orderId));
        UserAccount userAccount = userTransaction.getUserAccount();
        PaymentReceiptDto paymentReceiptDto = new PaymentReceiptDto(userAccount.getUserId(),
                userAccount.getUserName(),
                School.SKIPLY_SCHOOL.getName(),
                userTransaction.getProductCode(),
                formatDate(userTransaction.getCreatedDate()),
                userTransaction.getTransactionReference(),
                userAccount.getCardType().name(),
                userAccount.getCardNumber(), orderPurchaseDto.getOrderType().getName(),
                orderPurchaseDto.getAmount());
        log.info("Fetched payment receipt info for orderId={} successfully!", orderId);
        return paymentReceiptDto;
    }

   @Retry(name = "paymentReceiptService")
    public OrderPurchaseDto getOrder(String orderId) {
        try {
            log.info("Fetching order info..");
            return restTemplate.getForObject(ORDER_URL + "/" + orderId, OrderPurchaseDto.class);
        } catch (HttpClientErrorException e) {
            return null;
        }
    }

    private String formatDate(LocalDateTime dateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy, HH:mm");
        return dateTime.format(formatter);
    }

}
