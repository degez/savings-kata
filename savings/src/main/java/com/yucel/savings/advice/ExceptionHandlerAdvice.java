package com.yucel.savings.advice;

import com.yucel.savings.exception.AccountNotFoundException;
import com.yucel.savings.exception.BalanceCannotBeNegativeException;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@ControllerAdvice
@NoArgsConstructor
public class ExceptionHandlerAdvice {

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(AccountNotFoundException.class)
    public ResponseEntity<String> handleAccountNotFoundException(Exception ex, WebRequest request) {
        String responseMessage = ex.getMessage();
        log.error("error on balance operations: {}", responseMessage);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseMessage);
    }

    /**
     * request payload is valid, so not a bad request,
     * but we cannot process the entity
     */
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    @ExceptionHandler(BalanceCannotBeNegativeException.class)
    public ResponseEntity<String> handleBalanceCannotBeNegativeException(Exception ex, WebRequest request) {
        String responseMessage = ex.getMessage();
        log.error("error on balance update: {}", responseMessage);
        return ResponseEntity.unprocessableEntity().body(responseMessage);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleValidationExceptions(
            IllegalArgumentException ex) {
        String responseMessage = ex.getMessage();
        log.error("error on balance update: {}", responseMessage);
        return ResponseEntity.badRequest().body(responseMessage);
    }
}
