package com.min429.tourism_platform_server.common.service.board;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.min429.tourism_platform_server.admin.controller.dto.board.AdminBoardDetail;
import com.min429.tourism_platform_server.admin.controller.dto.board.AdminBoardThumbnail;
import com.min429.tourism_platform_server.admin.service.AdminBoardService;
import com.min429.tourism_platform_server.common.controller.dto.board.BoardPages;
import com.min429.tourism_platform_server.common.domain.board.Area;
import com.min429.tourism_platform_server.common.domain.board.Type;
import com.min429.tourism_platform_server.common.exception.common.IllegalArgumentException;
import com.min429.tourism_platform_server.common.exception.response.ErrorCode;
import com.min429.tourism_platform_server.user.controller.dto.board.UserBoardDetail;
import com.min429.tourism_platform_server.user.controller.dto.board.UserBoardThumbnail;
import com.min429.tourism_platform_server.user.service.UserBoardService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class BoardService {
	private final AdminBoardService adminBoardService;
	private final UserBoardService userBoardService;

	public BoardPages getSpotPages(Area area, Pageable adminPageable, Pageable userPageable) {
		Page<AdminBoardThumbnail> adminSpotThumbnails = adminBoardService.getAdminSpotsByArea(area, adminPageable);
		Page<UserBoardThumbnail> userSpotThumbnails = userBoardService.getUserSpotsByArea(area, userPageable);

		return getBoardPages(adminSpotThumbnails, userSpotThumbnails);
	}

	public BoardPages getAccommodationPages(Area area, Pageable adminPageable, Pageable userPageable) {
		Page<AdminBoardThumbnail> adminAccommodationThumbnails = adminBoardService.getAdminAccommodationsByArea(area,
			adminPageable);
		Page<UserBoardThumbnail> userAccommodationThumbnails = userBoardService.getUserAccommodationsByArea(area,
			userPageable);

		return getBoardPages(adminAccommodationThumbnails, userAccommodationThumbnails);
	}

	public BoardPages getRestaurantPages(Area area, Pageable adminPageable, Pageable userPageable) {
		Page<AdminBoardThumbnail> adminRestaurantThumbnails = adminBoardService.getAdminRestaurantsByArea(area,
			adminPageable);
		Page<UserBoardThumbnail> userRestaurantThumbnails = userBoardService.getUserRestaurantsByArea(area,
			userPageable);

		return getBoardPages(adminRestaurantThumbnails, userRestaurantThumbnails);
	}

	private BoardPages getBoardPages(Page<AdminBoardThumbnail> adminBoardThumbnails,
		Page<UserBoardThumbnail> userBoardThumbnails) {

		return BoardPages.builder()
			.adminBoards(adminBoardThumbnails)
			.userBoards(userBoardThumbnails)
			.build();
	}

	public BoardPages getBoardThumbnailPages(Area area, Type type, Pageable adminPageable, Pageable userPageable) {
		switch (type) {
			case SPOT:
				return getSpotPages(area, adminPageable, userPageable);
			case ACCOMMODATION:
				return getAccommodationPages(area, adminPageable, userPageable);
			case RESTAURANT:
				return getRestaurantPages(area, adminPageable, userPageable);
			default:
				throw new IllegalArgumentException(ErrorCode.BAD_REQUEST);
		}
	}

	public AdminBoardDetail getAdminBoardDetail(Type type, String boardId) {
		switch (type) {
			case SPOT:
				return adminBoardService.getSpotDetail(boardId);
			case ACCOMMODATION:
				return adminBoardService.getAccommodationDetail(boardId);
			case RESTAURANT:
				return adminBoardService.getRestaurantDetail(boardId);
			default:
				throw new IllegalArgumentException(ErrorCode.BAD_REQUEST);
		}
	}

	public UserBoardDetail getUserBoardDetail(Type type, String boardId) {
		switch (type) {
			case SPOT:
				return userBoardService.getSpotDetail(boardId);
			case ACCOMMODATION:
				return userBoardService.getAccommodationDetail(boardId);
			case RESTAURANT:
				return userBoardService.getRestaurantDetail(boardId);
			default:
				throw new IllegalArgumentException(ErrorCode.BAD_REQUEST);
		}
	}
}
