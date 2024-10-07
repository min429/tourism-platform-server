package com.min429.tourism_platform_server.common.exception.handler;

import static com.min429.tourism_platform_server.common.exception.response.ErrorResponse.*;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import com.min429.tourism_platform_server.common.exception.BaseException;
import com.min429.tourism_platform_server.common.exception.board.BoardException;
import com.min429.tourism_platform_server.common.exception.board.BoardNotFoundException;
import com.min429.tourism_platform_server.common.exception.common.CommonException;
import com.min429.tourism_platform_server.common.exception.response.ErrorResponse;
import com.min429.tourism_platform_server.common.exception.token.TokenException;
import com.min429.tourism_platform_server.common.exception.user.UserException;

import lombok.extern.slf4j.Slf4j;

/**
 * 전 계층 핸들러
 */
@Slf4j
@ControllerAdvice
public class CommonExceptionHandler {

	@ExceptionHandler
	public ResponseEntity<ErrorResponse> handleUserException(CommonException e) {
		log.error("[CommonException] ex", e);

		return entityFrom(e);
	}

	@ExceptionHandler
	public ResponseEntity<ErrorResponse> handleUserException(UserException e) {
		log.error("[UserException] ex", e);

		return entityFrom(e);
	}

	@ExceptionHandler
	public ResponseEntity<ErrorResponse> handleTokenException(TokenException e) {
		log.error("[TokenException] ex", e);

		return entityFrom(e);
	}

	@ExceptionHandler
	public ResponseEntity<ErrorResponse> handleBoardException(BoardException e) {
		log.error("[BoardException] ex", e);

		return entityFrom(e);
	}

	@ExceptionHandler({ BoardNotFoundException.class, NoResourceFoundException.class, MethodArgumentTypeMismatchException.class })
	public ModelAndView handleBoardNotFoundException(Exception e) {
		log.error("[Exception] ex", e);

		return new ModelAndView("error/error-404", HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler
	public ModelAndView handleException(Exception e) {
		log.error("[Exception] ex", e);

		return new ModelAndView("error/error-500", HttpStatus.INTERNAL_SERVER_ERROR);
	}

	private <T extends BaseException> ResponseEntity<ErrorResponse> entityFrom(T e){
		return ResponseEntity.status(e.getErrorCode().getStatus())
			.body(from(e.getErrorCode()));
	}
}
