package com.rakbank.payment.config;

import com.rakbank.commons.event.OrderEvent;
import com.rakbank.commons.constants.OrderStatus;
import com.rakbank.commons.event.PaymentEvent;
import com.rakbank.payment.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.function.Function;

@Configuration
public class PaymentConsumerConfig {

    @Autowired
    private PaymentService paymentService;

    @Bean
    public Function<Flux<OrderEvent>, Flux<PaymentEvent>> paymentProcessor() {
        return orderEventFlux -> orderEventFlux.flatMap(this::processPayment);
    }

    //If order status is Created, then process the order, otherwise cancel the order processing
    private Mono<PaymentEvent> processPayment(final OrderEvent orderEvent) {
        if(OrderStatus.ORDER_CREATED.equals(orderEvent.getOrderStatus())) {
            return  Mono.fromSupplier(() -> this.paymentService.newOrderEvent(orderEvent));
        }
        else {
            return Mono.fromRunnable(() -> this.paymentService.cancelOrderEvent(orderEvent));
        }
    }
}
