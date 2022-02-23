package com.rakbank.student.config;

import com.rakbank.commons.exception.StudentFeeManagementException;
import com.rakbank.commons.exception.ServiceError;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
@Slf4j
public class StudentExceptionHandler {

    @ExceptionHandler(StudentFeeManagementException.class)
    public @ResponseBody ResponseEntity<ServiceError> handleMhzRuntimeException(final StudentFeeManagementException e) {
        log.error("StudentFeeManagementException={}", e.getErrorMessage());

        ServiceError serviceError = new ServiceError(e.getErrorMessage().getErrorCode().toHttpStatusCode(),
                e.getErrorMessage().getMessage());

        return new ResponseEntity<>(serviceError, HttpStatus.valueOf(e.getErrorMessage().getErrorCode().toHttpStatusCode()));
    }

    @ExceptionHandler({HttpMessageNotReadableException.class, MethodArgumentNotValidException.class, HttpRequestMethodNotSupportedException.class})
    public @ResponseBody ResponseEntity<ServiceError> handleHttpException(final Exception e) {
        String rootMessage = ExceptionUtils.getRootCauseMessage(e);

        if(e instanceof MethodArgumentNotValidException) {
            log.error("Exception={}", rootMessage);
            return new ResponseEntity<>(methodArgumentNotValidException((MethodArgumentNotValidException) e), HttpStatus.BAD_REQUEST);
        } else {
            log.error("Exception={}", rootMessage, e);

            ServiceError serviceError = new ServiceError(400, rootMessage);

            return new ResponseEntity<>(serviceError, HttpStatus.BAD_REQUEST);
        }
    }

    @ExceptionHandler(Exception.class)
    public @ResponseBody ResponseEntity<ServiceError> handleException(final Exception e) {
        log.error("Exception={}", ExceptionUtils.getRootCauseMessage(e), e);

        ServiceError serviceError = new ServiceError(500, ExceptionUtils.getRootCauseMessage(e));
        return new ResponseEntity<>(serviceError, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private ServiceError methodArgumentNotValidException(final MethodArgumentNotValidException e) {
        StringBuilder stringBuilder = new StringBuilder();
        for(FieldError fieldError: e.getBindingResult().getFieldErrors()) {
            stringBuilder
                    .append(fieldError.getObjectName())
                    .append(".")
                    .append(fieldError.getField())
                    .append(" => ")
                    .append(fieldError.getDefaultMessage())
                    .append("; ");
        }

        for(ObjectError globalError : e.getBindingResult().getGlobalErrors()) {
            stringBuilder
                    .append(globalError.toString())
                    .append("; ");
        }
        return new ServiceError(400, stringBuilder.toString());
    }
}
