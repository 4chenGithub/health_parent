package com.health.Exception;

public class UserIdBeUseException extends RuntimeException {
    public UserIdBeUseException() {
    }

    public UserIdBeUseException(String message) {
        super(message);
    }
}
