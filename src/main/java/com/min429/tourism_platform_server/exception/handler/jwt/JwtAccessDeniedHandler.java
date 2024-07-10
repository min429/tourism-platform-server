package com.min429.tourism_platform_server.exception.handler.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.min429.tourism_platform_server.exception.ErrorResult;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import java.io.IOException;
import java.io.PrintWriter;

/**
 * 스프링 시큐리티 필터에서 인가 실패 시 예외 처리
 */
@Slf4j
public class JwtAccessDeniedHandler implements AccessDeniedHandler {

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException e) throws IOException, ServletException {
        log.error("[AccessDeniedException] ex", e);
//        response.sendError(HttpServletResponse.SC_FORBIDDEN, "접근 권한 없음"); // 에러 (default)컨트롤러로 다시 보내서 응답 처리

        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setContentType("application/json;charset=UTF-8");
        ErrorResult errorResult = new ErrorResult("EX", "접근 권한 없음");
        String json = objectMapper.writeValueAsString(errorResult);

        PrintWriter writer = response.getWriter();
        writer.write(json);
        writer.flush(); // WAS가 클라이언트에게 즉시 응답
    }
}
