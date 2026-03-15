package com.gyg.error;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Slf4j
public class ErrorHandler {

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handleBadRequest(IllegalArgumentException exp) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse("Bad Request", exp.getMessage(), System.currentTimeMillis()));
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handleException(ActivityNotFoundException exp) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponse("Activity Not Found", exp.getMessage(), System.currentTimeMillis()));
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handleException(Exception exp) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponse("Server Error", exp.getMessage(), System.currentTimeMillis()));
    }


}
