package com.rakbank.commons.exception;

import lombok.Data;

@Data
public class ErrorMessage {
    private ErrorCode errorCode;
    private String message;

    public ErrorMessage() { }

    public ErrorMessage(final ErrorCode errorCode, final String message, final Object... params) {
        this.errorCode = errorCode;
        this.message = String.format(message, params);
    }
}
