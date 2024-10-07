package com.min429.tourism_platform_server.user.controller.dto.board;

import java.util.List;

import org.springframework.lang.Nullable;
import org.springframework.web.multipart.MultipartFile;

public record UpdateBoardRequest(
	String title,
	@Nullable String description,
	@Nullable String address,
	@Nullable List<String> tags,
	@Nullable List<MultipartFile> images, // 새로 추가할 이미지
	@Nullable List<String> imageUrls // 저장되어 있던 이미지 url
) {
}
