package com.min429.tourism_platform_server.exception.handler.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.min429.tourism_platform_server.exception.ErrorResult;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import java.io.IOException;
import java.io.PrintWriter;

/**
 * 스프링 시큐리티 필터에서 인증 실패 시 예외 처리
 */
@Slf4j
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException e) throws IOException, ServletException {
        log.error("[AuthenticationException] ex", e);
//        response.sendError(HttpServletResponse.SC_FORBIDDEN, "접근 권한 없음"); // 에러 (default)컨트롤러로 다시 보내서 응답 처리

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json;charset=UTF-8");
        ErrorResult errorResult = new ErrorResult("EX", "인증 실패");
        String json = objectMapper.writeValueAsString(errorResult);

        PrintWriter writer = response.getWriter();
        writer.write(json);
        writer.flush(); // WAS가 클라이언트에게 즉시 응답
    }
}
