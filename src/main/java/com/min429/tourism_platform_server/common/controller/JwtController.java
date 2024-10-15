package com.min429.tourism_platform_server.common.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.min429.tourism_platform_server.common.service.jwt.JwtService;
import com.min429.tourism_platform_server.common.controller.dto.user.LogInResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("api/tokens")
public class JwtController {

	private final JwtService jwtService;

	@PostMapping("/create")
	public ResponseEntity<LogInResponse> refreshTokens(@RequestHeader("Authorization") String authorizationHeader) {
		return ResponseEntity.status(HttpStatus.CREATED).
			body(jwtService.refreshTokens(authorizationHeader));
	}
}
