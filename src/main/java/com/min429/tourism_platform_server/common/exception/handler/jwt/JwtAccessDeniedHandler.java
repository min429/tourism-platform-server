package com.min429.tourism_platform_server.common.exception.handler.jwt;

import static com.min429.tourism_platform_server.common.exception.response.ErrorResponse.*;

import java.io.IOException;
import java.io.PrintWriter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.min429.tourism_platform_server.common.exception.response.ErrorCode;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

/**
 * 스프링 시큐리티 필터에서 인가 실패 시 예외 처리
 */
@Slf4j
public class JwtAccessDeniedHandler implements AccessDeniedHandler {

	@Autowired
	private ObjectMapper objectMapper;

	@Override
	public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException e) throws
		IOException,
		ServletException {
		log.error("[AccessDeniedException] ex", e);

		response.setStatus(HttpServletResponse.SC_FORBIDDEN);
		response.setContentType("application/json;charset=UTF-8");
		String json = objectMapper.writeValueAsString(from(ErrorCode.USER_ACCESS_DENIED));

		PrintWriter writer = response.getWriter();
		writer.write(json);
		writer.flush(); // WAS가 클라이언트에게 즉시 응답
	}
}
