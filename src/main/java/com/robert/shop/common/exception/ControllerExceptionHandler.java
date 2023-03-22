package com.robert.shop.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;

@ControllerAdvice
@ResponseBody
public class ControllerExceptionHandler {

    @ExceptionHandler(ObjectNotFoundException.class)
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public ErrorMessage objectNotFoundException(ObjectNotFoundException ex, WebRequest request) {
        return errorMessage(
                HttpStatus.NOT_FOUND.value(),
                ex,
                request
        );
    }

    @ExceptionHandler(ObjectNotIdenticalException.class)
    @ResponseStatus(value = HttpStatus.NOT_ACCEPTABLE)
    public ErrorMessage objectNotIdenticalException(ObjectNotIdenticalException ex, WebRequest request) {
        return errorMessage(
                HttpStatus.NOT_ACCEPTABLE.value(),
                ex,
                request
        );
    }

    @ExceptionHandler(LinkExpiredException.class)
    @ResponseStatus(value = HttpStatus.NOT_ACCEPTABLE)
    public ErrorMessage linkExpiredException(LinkExpiredException ex, WebRequest request) {
        return errorMessage(
                HttpStatus.NOT_ACCEPTABLE.value(),
                ex,
                request
        );
    }

    @ExceptionHandler(PaymentMethodP24Exception.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ErrorMessage paymentMethodP24Exception(PaymentMethodP24Exception ex, WebRequest request) {
        return errorMessage(
                HttpStatus.BAD_REQUEST.value(),
                ex,
                request
        );
    }

    private ErrorMessage errorMessage(int statusCode, RuntimeException ex, WebRequest request) {
        return new ErrorMessage(
                statusCode,
                LocalDateTime.now(),
                ex.getMessage(),
                request.getDescription(false)
        );
    }

}
