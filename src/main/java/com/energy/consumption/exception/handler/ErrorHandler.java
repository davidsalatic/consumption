package com.energy.consumption.exception.handler;

import com.energy.consumption.exception.model.CsvParseException;
import com.energy.consumption.exception.model.ErrorResponse;
import com.energy.consumption.exception.model.ValidationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class ErrorHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException exception) {
        exception.printStackTrace();

        String message = "Request failed field validation: " + exception.getMessage();
        HttpStatus status = HttpStatus.BAD_REQUEST;

        ErrorResponse errorResponse = new ErrorResponse(status, status.value(), message, LocalDateTime.now());

        return new ResponseEntity<>(errorResponse, status);
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(ValidationException exception) {
        exception.printStackTrace();

        String message = "Request failed: " + exception.getMessage();
        HttpStatus status = HttpStatus.BAD_REQUEST;

        ErrorResponse errorResponse = new ErrorResponse(status, status.value(), message, LocalDateTime.now());

        return new ResponseEntity<>(errorResponse, status);
    }

    @ExceptionHandler(CsvParseException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorResponse> handleCsvParseException(CsvParseException exception) {
        exception.printStackTrace();

        String message = "Parse CSV failed: " + exception.getMessage();
        HttpStatus status = HttpStatus.BAD_REQUEST;

        ErrorResponse errorResponse = new ErrorResponse(status, status.value(), message, LocalDateTime.now());

        return new ResponseEntity<>(errorResponse, status);
    }
}
