package com.min429.tourism_platform_server.user.controller.dto.board;

import java.util.List;

import org.springframework.lang.Nullable;
import org.springframework.web.multipart.MultipartFile;

public record CreateBoardRequest(
	String title,
	@Nullable String description,
	@Nullable String address,
	@Nullable List<String> tags,
	@Nullable List<MultipartFile> images
) {
}
