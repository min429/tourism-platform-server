package com.min429.tourism_platform_server.common.exception.token;

import com.min429.tourism_platform_server.common.exception.response.ErrorCode;

public class TokenValidationException extends TokenException {
	public TokenValidationException(final ErrorCode errorCode) {
		super(errorCode);
	}
}
