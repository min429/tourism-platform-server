package com.min429.tourism_platform_server.user.domain;

import org.springframework.data.mongodb.core.mapping.Document;

import com.min429.tourism_platform_server.common.domain.board.BoardImage;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Document("user-board-image")
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class UserBoardImage extends BoardImage {
	private String boardId;
}
