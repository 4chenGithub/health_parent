package com.health.Exception;

public class MenuIsExistException extends RuntimeException {
    public MenuIsExistException() {
    }

    public MenuIsExistException(String message) {
        super(message);
    }
}
