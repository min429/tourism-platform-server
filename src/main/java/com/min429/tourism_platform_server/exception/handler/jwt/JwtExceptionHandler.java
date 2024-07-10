package com.min429.tourism_platform_server.exception.handler.jwt;

import com.min429.tourism_platform_server.exception.ErrorResult;
import com.min429.tourism_platform_server.exception.TokenValidationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 컨트롤러에서 토큰 검증 실패 시 예외처리
 */
@Slf4j
@RestControllerAdvice
public class JwtExceptionHandler {

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler
    public ResponseEntity<ErrorResult> handleTokenValidationException(TokenValidationException e) {
        log.error("[TokenValidationException] ex", e);
        return ResponseEntity.badRequest()
                .body(new ErrorResult("EX", e.getMessage())); // 400 Bad Request
    }
}
