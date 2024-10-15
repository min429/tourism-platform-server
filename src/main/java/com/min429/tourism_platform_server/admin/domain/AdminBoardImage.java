package com.min429.tourism_platform_server.admin.domain;

import org.springframework.data.mongodb.core.mapping.Document;

import com.min429.tourism_platform_server.common.domain.board.BoardImage;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Document("admin-board-image")
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class AdminBoardImage extends BoardImage {
	private Long contentId;
	private String imageName;
	private String serialNum;
	private String cpyrhtDivCd;
}
