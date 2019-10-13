package com.health.Exception;

public class CheckItemBeUseException extends RuntimeException {
    public CheckItemBeUseException() {
    }

    public CheckItemBeUseException(String message) {
        super(message);
    }
}
