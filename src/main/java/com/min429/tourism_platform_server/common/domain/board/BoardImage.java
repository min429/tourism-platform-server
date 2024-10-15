package com.min429.tourism_platform_server.common.domain.board;

import java.util.List;

import org.springframework.data.annotation.Id;

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
public abstract class BoardImage {
	@Id
	private String id;

	private List<String> originalImageUrls;
	private List<String> smallImageUrls;
}
