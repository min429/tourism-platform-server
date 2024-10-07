package com.min429.tourism_platform_server.common.exception.user;

import com.min429.tourism_platform_server.common.exception.BaseException;
import com.min429.tourism_platform_server.common.exception.response.ErrorCode;

public abstract class UserException extends BaseException {
	public UserException(final ErrorCode errorCode) {super(errorCode);}
}
