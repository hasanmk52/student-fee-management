package com.rakbank.commons.exception;

public enum ErrorCode {
    NOT_AUTHORIZED,
    NOT_AUTHENTICATED,
    NOT_ALLOWED,
    ALREADY_EXISTS,
    BAD_DATA,
    BAD_STATE,
    MISSING_DATA,
    NOT_FOUND,
    FORMAT_ERROR,
    INTERNAL_SERVER_ERROR,
    SERVICE_UNAVAILABLE(),
    SERVICE_BUSY();

    ErrorCode() { }

    public int toHttpStatusCode() {
        switch(this) {
            case NOT_AUTHORIZED:
            case NOT_AUTHENTICATED: return 401;
            case ALREADY_EXISTS: return 409;
            case BAD_DATA:
            case BAD_STATE:
            case MISSING_DATA:
            case FORMAT_ERROR: return 400;
            case NOT_FOUND: return 404;
            case NOT_ALLOWED: return 405;
            case SERVICE_UNAVAILABLE:
            case SERVICE_BUSY: return 503;
            case INTERNAL_SERVER_ERROR:
            default: return 500;
        }
    }
}
