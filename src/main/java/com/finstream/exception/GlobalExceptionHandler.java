package com.finstream.exception;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(TradeNotFoundException.class)
    public ResponseEntity<String> handleNotFound(TradeNotFoundException ex) {
        return ResponseEntity.status(404).body(ex.getMessage());
    }

    @ExceptionHandler(LimitExceededException.class)
    public ResponseEntity<String> handleLimit(LimitExceededException ex) {
        return ResponseEntity.status(400).body(ex.getMessage());
    }

    @ExceptionHandler(TradeException.class)
    public ResponseEntity<String> handleTrade(TradeException ex) {
        return ResponseEntity.status(400).body(ex.getMessage());
    }
}
