package com.rakbank.order.config;

import com.rakbank.commons.event.PaymentEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.UUID;
import java.util.function.Consumer;

@Configuration
public class EventConsumerConfig {

    @Autowired
    private OrderStatusUpdateHandler handler;

    /*
     * Listen payment-event-topic
     * Will check payment status
     * If payment status completed -> complete the order
     * If payment status failed -> cancel the order
     * */
    @Bean
    public Consumer<PaymentEvent> paymentEventConsumer(){

        return (payment)-> handler.updateOrder(UUID.fromString(payment.getPaymentRequestDto().getOrderId()),
                po -> po.setPaymentStatus(payment.getPaymentStatus()));
    }
}
