package com.min429.tourism_platform_server.common.exception.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

	BAD_REQUEST(ErrorConstant.BAD_REQUEST, "ERROR-400", "잘못된 요청입니다."),
	INTERNAL_SERVER(ErrorConstant.INTERNAL_SERVER_ERROR, "ERROR-500", "서버 내부 오류입니다."),
	FIREBASE_ERROR(ErrorConstant.INTERNAL_SERVER_ERROR, "ERROR-501", "파이어베이스 오류입니다."),
	ENCODING_ERROR(ErrorConstant.INTERNAL_SERVER_ERROR, "ERROR-502", "인코딩 오류입니다."),

	// 네트워크
	HTTP_CLIENT_REQUEST_FAILED(ErrorConstant.INTERNAL_SERVER_ERROR, "NETWORK-001", "서버 요청에 실패하였습니다."),

	// 유저
	USER_NOT_FOUND(ErrorConstant.NOT_FOUND, "USER-001", "존재하지 않는 회원입니다."),
	USER_ACCESS_DENIED(ErrorConstant.FORBIDDEN, "USER-002", "접근 권한이 없습니다."),
	USER_NOT_APPROVED(ErrorConstant.UNAUTHORIZED, "USER-003", "인증되지 않은 유저입니다."),

	// 토큰
	INVALID_TOKEN(ErrorConstant.UNAUTHORIZED, "TOKEN-001", "유효하지 않은 토큰입니다."),
	TOKEN_EXPIRED(ErrorConstant.UNAUTHORIZED, "TOKEN-002", "만료된 토큰입니다."),
	REFRESH_TOKEN_NOT_FOUND(ErrorConstant.NOT_FOUND, "TOKEN-003", "리프레시 토큰을 찾을 수 없습니다."),
	REFRESH_TOKEN_EXPIRED(ErrorConstant.UNAUTHORIZED, "TOKEN-004", "만료된 리프레시 토큰입니다."),

	// 게시판
	BOARD_NOT_FOUND(ErrorConstant.NOT_FOUND, "BOARD-001", "존재하지 않는 게시글입니다.");

	private final Integer status;
	private final String code;
	private final String message;
}
