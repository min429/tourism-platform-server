package com.min429.tourism_platform_server.common.domain.board;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.domain.Page;

import com.min429.tourism_platform_server.admin.controller.dto.board.AdminBoardDetail;
import com.min429.tourism_platform_server.admin.controller.dto.board.AdminBoardThumbnail;
import com.min429.tourism_platform_server.user.controller.dto.board.UserBoardDetail;
import com.min429.tourism_platform_server.user.controller.dto.board.UserBoardThumbnail;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class Info {

	@Id
	private String id;

	private String addr1; // 주소
	private Area area; // 지역코드
	private String cat1; // 대분류 코드
	private String cat2; // 중분류 코드
	private String cat3; // 소분류 코드
	private Long contentid; // 콘텐츠 ID
	private ContentType contentType; // 콘텐츠 타입 ID
	private String firstimage; // 대표이미지
	private Double mapx; // GPS X좌표
	private Double mapy; // GPS Y좌표
	private Sigungu sigungu; // 시군구코드
	private String title; // 콘텐츠제목
	private String tel; // 전화번호
	private String overview; // 소개

	public List<String> getTags() {
		return null;
	}

	public String getNickname() {
		return null;
	}

	public static <T extends Info> Page<UserBoardThumbnail> getUserBoardThumbnailPages(Page<T> infos, Type type) {
		return infos.map(info -> UserBoardThumbnail.builder()
			.boardId(info.getId())
			.title(info.getTitle())
			.area(info.getArea())
			.type(type)
			.contentType(info.getContentType())
			.addr1(info.getAddr1())
			.firstimage(info.getFirstimage())
			.overview(info.getOverview())
			.tags(info.getTags())
			.nickname(info.getNickname())
			.build());
	}

	public static <T extends Info> Page<AdminBoardThumbnail> getAdminBoardThumbnailPages(Page<T> infos, Type type) {
		return infos.map(info -> AdminBoardThumbnail.builder()
			.boardId(info.getId())
			.title(info.getTitle())
			.area(info.getArea())
			.type(type)
			.contentType(info.getContentType())
			.addr1(info.getAddr1())
			.firstimage(info.getFirstimage())
			.overview(info.getOverview())
			.build());
	}

	public static <T extends Info> AdminBoardDetail getAdminBoardDetail(T info, List<String> images) {
		return AdminBoardDetail.builder()
			.boardId(info.getId())
			.title(info.getTitle())
			.area(info.getArea())
			.contentType(info.getContentType())
			.firstimage(info.getFirstimage())
			.addr1(info.getAddr1())
			.mapx(info.getMapx())
			.mapy(info.getMapy())
			.sigungu(info.getSigungu())
			.tel(info.getTel())
			.overview(info.getOverview())
			.images(images)
			.build();
	}

	public static <T extends Info> UserBoardDetail getUserBoardDetail(T info, List<String> images) {
		return UserBoardDetail.builder()
			.boardId(info.getId())
			.title(info.getTitle())
			.area(info.getArea())
			.contentType(info.getContentType())
			.firstimage(info.getFirstimage())
			.addr1(info.getAddr1())
			.mapx(info.getMapx())
			.mapy(info.getMapy())
			.tel(info.getTel())
			.overview(info.getOverview())
			.nickname(info.getNickname())
			.tags(info.getTags())
			.images(images)
			.build();
	}
}
