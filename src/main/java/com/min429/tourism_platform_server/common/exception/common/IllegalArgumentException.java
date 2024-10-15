package com.min429.tourism_platform_server.common.exception.common;

import com.min429.tourism_platform_server.common.exception.response.ErrorCode;

public class IllegalArgumentException extends CommonException {
	public IllegalArgumentException(ErrorCode errorCode) {
		super(errorCode);
	}
}
