package com.min429.tourism_platform_server.exception;

import org.springframework.security.core.AuthenticationException;

public class TokenValidationException extends AuthenticationException {
    public TokenValidationException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public TokenValidationException(String msg) {
        super(msg);
    }
}
