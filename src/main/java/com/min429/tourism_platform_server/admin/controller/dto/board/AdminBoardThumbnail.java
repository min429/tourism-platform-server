package com.min429.tourism_platform_server.admin.controller.dto.board;

import com.min429.tourism_platform_server.common.domain.board.Area;
import com.min429.tourism_platform_server.common.domain.board.ContentType;
import com.min429.tourism_platform_server.common.domain.board.Type;

import lombok.Builder;

@Builder
public record AdminBoardThumbnail(
	String boardId,
	String title, // 콘텐츠명 (제목)
	Area area, // 지역
	Type type,
	ContentType contentType, // 분류 타입
	String addr1, // 주소
	String firstimage, // 대표 이미지 (원본) URL
	String overview // 소개
) {
}
