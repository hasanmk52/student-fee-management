package com.rakbank.commons.exception;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ServiceError {

    private Integer statusCode;
    private String errorMessage;
}
