package com.min429.tourism_platform_server.exception.handler;

import com.min429.tourism_platform_server.controller.UserController;
import com.min429.tourism_platform_server.exception.ErrorResult;
import com.min429.tourism_platform_server.exception.UserEmailException;
import com.min429.tourism_platform_server.exception.UserPasswordException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice(assignableTypes = UserController.class)
public class UserExceptionHandler {
    @ExceptionHandler
    public ResponseEntity<ErrorResult> handleUserEmailException(UserEmailException e) {
        log.error("[UserEmailException] ex", e);
        return ResponseEntity.badRequest()
                .body(new ErrorResult("EX", e.getMessage())); // 400 Bad Request
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResult> handleUserPasswordException(UserPasswordException e) {
        log.error("[UserPasswordException] ex", e);
        return ResponseEntity.badRequest()
                .body(new ErrorResult("EX", e.getMessage())); // 400 Bad Request
    }
}
