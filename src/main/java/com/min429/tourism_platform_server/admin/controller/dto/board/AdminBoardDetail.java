package com.min429.tourism_platform_server.admin.controller.dto.board;

import java.util.List;

import com.min429.tourism_platform_server.common.domain.board.Area;
import com.min429.tourism_platform_server.common.domain.board.ContentType;
import com.min429.tourism_platform_server.common.domain.board.Sigungu;

import lombok.Builder;

@Builder
public record AdminBoardDetail(
	String boardId,
	String title, // 콘텐츠명 (제목)
	Area area, // 지역
	ContentType contentType, // 분류 타입
	String firstimage, // 대표 이미지 (원본) URL
	String addr1, // 주소
	Double mapx, // GPS x좌표
	Double mapy, // GPS y좌표
	Sigungu sigungu, // 시군구
	String tel, // 전화번호
	String overview, // 소개
	String nickname, // 글쓴이
	List<String> tags, // 태그
	List<String> images // 서브 이미지
) {
}
