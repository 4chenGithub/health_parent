package com.health.Exception;

public class CheckGroupBeUseException extends RuntimeException {
    public CheckGroupBeUseException() {
    }

    public CheckGroupBeUseException(String message) {
        super(message);
    }
}
