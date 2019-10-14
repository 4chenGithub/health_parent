package com.health.Exception;

public class UsernameBeUseException extends RuntimeException {
    public UsernameBeUseException() {
    }

    public UsernameBeUseException(String message) {
        super(message);
    }
}
