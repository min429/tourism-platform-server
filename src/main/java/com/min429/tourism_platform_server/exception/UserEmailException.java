package com.min429.tourism_platform_server.exception;

public class UserEmailException extends RuntimeException {
    public UserEmailException(String message) {
        super(message);
    }

    public UserEmailException(String message, Throwable cause) {
        super(message, cause);
    }
}
