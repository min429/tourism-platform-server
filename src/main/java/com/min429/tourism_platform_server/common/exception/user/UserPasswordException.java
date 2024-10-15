package com.min429.tourism_platform_server.common.exception.user;

import com.min429.tourism_platform_server.common.exception.response.ErrorCode;

public class UserPasswordException extends UserException {
	public UserPasswordException(final ErrorCode errorCode) {
		super(errorCode);
	}
}
