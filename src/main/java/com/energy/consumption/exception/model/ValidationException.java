package com.energy.consumption.exception.model;

public class ValidationException extends RuntimeException {

    public ValidationException(String message) {
        super(message);
    }
}
