package com.example.Order_Service.exception;

public class DuplicateOrderException extends RuntimeException {

    public DuplicateOrderException(String message) {
        super(message);
    }
}