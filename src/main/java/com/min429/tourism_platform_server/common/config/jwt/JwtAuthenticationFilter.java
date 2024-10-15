package com.min429.tourism_platform_server.common.config.jwt;

import java.io.IOException;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import com.min429.tourism_platform_server.common.exception.handler.jwt.JwtAuthenticationEntryPoint;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

/**
 * 토큰 검증 및 사용자 인증 정보를 저장하는 필터
 */
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

	private final static String HEADER_AUTHORIZATION = "Authorization";
	private final JwtProvider jwtProvider;
	private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

	/**
	 * 스프링 시큐리티 필터에서 인증 처리
	 **/
	@Override
	protected void doFilterInternal(
		HttpServletRequest request,
		HttpServletResponse response,
		FilterChain filterChain) throws ServletException, IOException {

		// "/api/users/signup", "/api/users/login" 경로는 인증과 상관없이 허용
		if (request.getRequestURI().startsWith("/api/users/signup")
			|| request.getRequestURI().startsWith("/api/users/login")
			|| request.getRequestURI().matches(".*")
		) {
			filterChain.doFilter(request, response);
			return;
		}

		String authorizationHeader = request.getHeader(HEADER_AUTHORIZATION);
		String token = jwtProvider.getToken(authorizationHeader); // 헤더에서 액세스 토큰 가져옴

		try {
			jwtProvider.ValidateToken(token);
		} catch (AuthenticationException e) {
			jwtAuthenticationEntryPoint.commence(request, response, e);
			return;
		}

		// 사용자 인증 정보 - Principal(신원 ex. 아이디), Credentials(자격 증명 ex. 비밀번호), Authorities(권한)
		Authentication authentication = jwtProvider.getAuthentication(token);
		// SecurityContext에 사용자 인증 정보 저장 -> 언제든 전역에서 접근 가능
		SecurityContextHolder.getContext().setAuthentication(authentication);

		filterChain.doFilter(request, response);
	}
}
