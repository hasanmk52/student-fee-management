package com.rakbank.commons.exception;

public class StudentFeeManagementException extends RuntimeException {

    protected final ErrorMessage errorMessage;

    public StudentFeeManagementException(final ErrorCode errorCode, final String message, final Object... args) {
        super(String.format(message != null ? message : "No error message defined.", args));
        errorMessage = new ErrorMessage(errorCode, message, args);
    }

    public StudentFeeManagementException(final Throwable throwable, final ErrorCode errorCode, final String message, final Object... args) {
        super(String.format(message, args), throwable);
        errorMessage = new ErrorMessage(errorCode, message, args);
    }

    public StudentFeeManagementException(final ErrorMessage errorMessage) {
        super(errorMessage.getMessage());
        this.errorMessage = errorMessage;
    }

    public ErrorMessage getErrorMessage() {
        return errorMessage;
    }

}