package com.health.Exception;

public class MenuBeUseException extends RuntimeException {
    public MenuBeUseException() {
    }

    public MenuBeUseException(String message) {
        super(message);
    }
}
