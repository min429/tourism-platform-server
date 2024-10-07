package com.min429.tourism_platform_server.common.controller.dto.user;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LogInResponse {
	private Long userId;
	private String userName;
	private String accessToken;
	private String refreshToken;
}
