package com.min429.tourism_platform_server.common.exception.response;

import org.springframework.http.HttpStatus;

public class ErrorConstant {

	public static Integer UNAUTHORIZED = HttpStatus.UNAUTHORIZED.value();

	public static Integer FORBIDDEN = HttpStatus.FORBIDDEN.value();

	public static Integer BAD_REQUEST = HttpStatus.BAD_REQUEST.value();

	public static Integer NOT_FOUND = HttpStatus.NOT_FOUND.value();

	public static Integer INTERNAL_SERVER_ERROR = HttpStatus.INTERNAL_SERVER_ERROR.value();
}

