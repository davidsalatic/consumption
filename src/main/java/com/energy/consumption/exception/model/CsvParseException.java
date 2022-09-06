package com.energy.consumption.exception.model;

public class CsvParseException extends RuntimeException {

    public CsvParseException(String message, Throwable cause) {
        super(message, cause);
    }
}
