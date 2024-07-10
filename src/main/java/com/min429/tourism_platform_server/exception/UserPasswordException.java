package com.min429.tourism_platform_server.exception;

public class UserPasswordException extends RuntimeException {
    public UserPasswordException(String message) {
        super(message);
    }

    public UserPasswordException(String message, Throwable cause) {
        super(message, cause);
    }
}
