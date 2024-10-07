package com.min429.tourism_platform_server.common.exception.board;

import com.min429.tourism_platform_server.common.exception.BaseException;
import com.min429.tourism_platform_server.common.exception.response.ErrorCode;

public class BoardNotFoundException extends BaseException {
	public BoardNotFoundException(final ErrorCode errorCode) {
		super(errorCode);
	}
}
