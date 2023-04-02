package com.robert.shop.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.NoSuchElementException;

@ControllerAdvice
@ResponseBody
public class ControllerExceptionHandler {

    @ExceptionHandler(ObjectNotFoundException.class)
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public ErrorMessage objectNotFoundException(ObjectNotFoundException ex, HttpServletRequest request) {
        return errorMessage(
                ex,
                HttpStatus.NOT_FOUND,
                request
        );
    }

    @ExceptionHandler(ObjectNotIdenticalException.class)
    @ResponseStatus(value = HttpStatus.NOT_ACCEPTABLE)
    public ErrorMessage objectNotIdenticalException(ObjectNotIdenticalException ex, HttpServletRequest request) {
        return errorMessage(
                ex,
                HttpStatus.NOT_ACCEPTABLE,
                request
        );
    }

    @ExceptionHandler(LinkExpiredException.class)
    @ResponseStatus(value = HttpStatus.NOT_ACCEPTABLE)
    public ErrorMessage linkExpiredException(LinkExpiredException ex, HttpServletRequest request) {
        return errorMessage(
                ex,
                HttpStatus.NOT_ACCEPTABLE,
                request
        );
    }

    @ExceptionHandler(PaymentMethodP24Exception.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ErrorMessage paymentMethodP24Exception(PaymentMethodP24Exception ex, HttpServletRequest request) {
        return errorMessage(
                ex,
                HttpStatus.BAD_REQUEST,
                request
        );
    }

    @ExceptionHandler(NoSuchElementException.class)
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public ErrorMessage noSuchElementException(NoSuchElementException ex, HttpServletRequest request) {
        return errorMessage(
                ex,
                HttpStatus.NOT_FOUND,
                request
        );
    }

    private ErrorMessage errorMessage(RuntimeException ex, HttpStatus httpStatus, HttpServletRequest request) {
        return new ErrorMessage(
                LocalDateTime.now(),
                httpStatus.value(),
                httpStatus.getReasonPhrase(),
                ex.getMessage(),
                request.getRequestURI()
        );
    }

}
