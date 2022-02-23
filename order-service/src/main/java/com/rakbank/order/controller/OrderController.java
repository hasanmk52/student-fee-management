package com.rakbank.order.controller;

import com.rakbank.commons.dto.OrderPurchaseDto;
import com.rakbank.commons.dto.OrderRequestDto;
import com.rakbank.commons.exception.ErrorCode;
import com.rakbank.commons.exception.StudentFeeManagementException;
import com.rakbank.order.entity.OrderPurchase;
import com.rakbank.order.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/order")
@AllArgsConstructor
@Slf4j
public class OrderController {

    private final OrderService orderService;

    @Operation(summary = "Create Order", description = "Create a new Fee Order for studentId and studentGrade", tags = "Create")
    @PostMapping(value = "/create", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<OrderPurchaseDto> createPurchase(@Valid @RequestBody final OrderRequestDto orderRequestDto){
        log.info("Received order request={}", orderRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(convertEntityToDto(orderService.createOrder(orderRequestDto)));
    }

    @Operation(summary = "Get All Orders", description = "Get All Orders", tags = "Get")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Fetched all orders",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = OrderPurchaseDto.class))}),
            @ApiResponse(responseCode = "404", description = "Orders not found", content = @Content)})
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<OrderPurchaseDto> getOrders(){
        return orderService.getAllOrders().stream()
                .map(this::convertEntityToDto)
                .collect(Collectors.toList());
    }

    @Operation(summary = "Get Order Details", description = "Get Order Details for a particular orderId", tags = "Get")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Fetched order details",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = OrderPurchaseDto.class))}),
            @ApiResponse(responseCode = "404", description = "Order not found", content = @Content),
            @ApiResponse(responseCode = "400", description = "Order Details cannot be fetched", content = @Content)})
    @GetMapping(value = "/{orderId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public OrderPurchaseDto getOrderDetails(@PathVariable String orderId) {
        Optional<OrderPurchase> orderOptional = orderService.find(orderId);
        if(!orderOptional.isPresent()) {
            throw new StudentFeeManagementException(ErrorCode.BAD_DATA, "Cannot find Order with orderId={}", orderId);
        }
        return convertEntityToDto(orderOptional.get());
    }

    private OrderPurchaseDto convertEntityToDto(OrderPurchase entity) {
        return new OrderPurchaseDto(entity.getId().toString(),
                entity.getStudentId(),
                entity.getStudentGrade(),
                entity.getOrderType(),
                entity.getAmount(),
                entity.getOrderStatus(),
                entity.getPaymentStatus(),
                entity.getCreatedDate());
    }
}
