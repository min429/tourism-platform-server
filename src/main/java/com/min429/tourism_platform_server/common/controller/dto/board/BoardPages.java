package com.min429.tourism_platform_server.common.controller.dto.board;

import org.springframework.data.domain.Page;

import lombok.Builder;

@Builder
public record BoardPages(
	Page<?> adminBoards,
	Page<?> userBoards
) {
}
