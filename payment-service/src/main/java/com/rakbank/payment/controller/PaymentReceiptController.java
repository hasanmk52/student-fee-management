package com.rakbank.payment.controller;

import com.rakbank.commons.dto.PaymentReceiptDto;
import com.rakbank.payment.service.PaymentReceiptService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/payment-receipt")
@AllArgsConstructor
public class PaymentReceiptController {

    private final PaymentReceiptService paymentReceiptService;

    @Operation(summary = "Get Payment Receipt Info", description = "Get Payment Receipt Info for a particular oder", tags = "Get")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Fetched receipt info",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = PaymentReceiptDto.class))}),
            @ApiResponse(responseCode = "404", description = "Receipt info not found", content = @Content),
            @ApiResponse(responseCode = "400", description = "Receipt info cannot be fetched", content = @Content)})
    @GetMapping(value = "/{orderId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public PaymentReceiptDto fetchPaymentReceipt(
            @Parameter(description = "Order Id for the order you need to retrieve", example = "f7ee5182-cc7f-4c18-b927-5739dd9a426d", required = true) @PathVariable String orderId) {
       return paymentReceiptService.fetchPaymentReceipt(orderId);
    }

}
