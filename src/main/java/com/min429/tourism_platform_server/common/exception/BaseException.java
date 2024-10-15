package com.min429.tourism_platform_server.common.exception;

import com.min429.tourism_platform_server.common.exception.response.ErrorCode;

import lombok.Getter;

@Getter
public abstract class BaseException extends RuntimeException {
	private final ErrorCode errorCode;

	public BaseException(ErrorCode errorCode) {
		super(errorCode.getMessage());

		this.errorCode = errorCode;
	}
}
