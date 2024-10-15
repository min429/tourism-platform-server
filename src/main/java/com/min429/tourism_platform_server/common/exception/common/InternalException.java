package com.min429.tourism_platform_server.common.exception.common;

import com.min429.tourism_platform_server.common.exception.response.ErrorCode;

public class InternalException extends CommonException {
	public InternalException(ErrorCode errorCode) {
		super(errorCode);
	}
}
