package com.health.Exception;

public class TelephoneIsExistException extends RuntimeException {
    public TelephoneIsExistException() {
    }

    public TelephoneIsExistException(String message) {
        super(message);
    }
}
