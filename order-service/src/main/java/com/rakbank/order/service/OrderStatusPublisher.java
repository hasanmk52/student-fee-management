package com.rakbank.order.service;

import com.rakbank.commons.constants.OrderStatus;
import com.rakbank.commons.dto.OrderRequestDto;
import com.rakbank.commons.event.OrderEvent;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Sinks;

@Component
@AllArgsConstructor
@Slf4j
public class OrderStatusPublisher {

    private Sinks.Many<OrderEvent> orderSinks;

    public void publishOrderEvent(final OrderRequestDto orderRequestDto, final OrderStatus orderStatus){
        OrderEvent orderEvent = new OrderEvent(orderRequestDto, orderStatus);
        log.info("Publishing order event with order status={} for order={}", orderStatus, orderRequestDto);
        orderSinks.tryEmitNext(orderEvent);
    }
}
