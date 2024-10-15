package com.min429.tourism_platform_server.common.exception.common;

import com.min429.tourism_platform_server.common.exception.BaseException;
import com.min429.tourism_platform_server.common.exception.response.ErrorCode;

public class CommonException extends BaseException {
	public CommonException(ErrorCode errorCode) {super(errorCode);}
}
