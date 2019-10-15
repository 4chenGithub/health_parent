package com.health.Exception;

public class PackageBeUseException extends RuntimeException {
    public PackageBeUseException() {
    }

    public PackageBeUseException(String message) {
        super(message);
    }
}
