package com.rakbank.commons.event;

import com.rakbank.commons.constants.OrderStatus;
import com.rakbank.commons.dto.OrderRequestDto;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@NoArgsConstructor
@Data
public class OrderEvent implements Event {

    private UUID eventId;
    private LocalDateTime eventDate;
    private OrderRequestDto orderRequestDto;
    private OrderStatus orderStatus;

    @Override
    public UUID getEventId() {
        return eventId;
    }

    @Override
    public LocalDateTime getDate() {
        return eventDate;
    }

    public OrderEvent(OrderRequestDto orderRequestDto, OrderStatus orderStatus) {
        this.eventId = UUID.randomUUID();
        this.eventDate = LocalDateTime.now();
        this.orderRequestDto = orderRequestDto;
        this.orderStatus = orderStatus;
    }
}
