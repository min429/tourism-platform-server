package com.min429.tourism_platform_server.common.exception.token;

import com.min429.tourism_platform_server.common.exception.BaseException;
import com.min429.tourism_platform_server.common.exception.response.ErrorCode;

public abstract class TokenException extends BaseException {
	public TokenException(final ErrorCode errorCode) {super(errorCode);}
}
