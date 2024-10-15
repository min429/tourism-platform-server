package com.min429.tourism_platform_server.admin.service;

import static com.min429.tourism_platform_server.common.domain.board.Info.*;
import static com.min429.tourism_platform_server.common.domain.board.Type.*;
import static java.util.Collections.*;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.min429.tourism_platform_server.admin.controller.dto.board.AdminBoardDetail;
import com.min429.tourism_platform_server.admin.controller.dto.board.AdminBoardThumbnail;
import com.min429.tourism_platform_server.admin.domain.AdminAccommodation;
import com.min429.tourism_platform_server.admin.domain.AdminBoardImage;
import com.min429.tourism_platform_server.admin.domain.AdminRestaurant;
import com.min429.tourism_platform_server.admin.domain.AdminSpot;
import com.min429.tourism_platform_server.admin.repository.mongo.AdminAccommodationRepository;
import com.min429.tourism_platform_server.admin.repository.mongo.AdminBoardImageRepository;
import com.min429.tourism_platform_server.admin.repository.mongo.AdminRestaurantRepository;
import com.min429.tourism_platform_server.admin.repository.mongo.AdminSpotRepository;
import com.min429.tourism_platform_server.common.domain.board.Area;
import com.min429.tourism_platform_server.common.domain.board.Info;
import com.min429.tourism_platform_server.common.exception.board.BoardNotFoundException;
import com.min429.tourism_platform_server.common.exception.response.ErrorCode;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class AdminBoardService {
	private final AdminSpotRepository adminSpotRepository;
	private final AdminRestaurantRepository adminRestaurantRepository;
	private final AdminAccommodationRepository adminAccommodationRepository;
	private final AdminBoardImageRepository adminBoardImageRepository;

	public Page<AdminBoardThumbnail> getAdminSpotsByArea(Area area, Pageable pageable) {
		Page<AdminSpot> spots = adminSpotRepository.findAllByArea(area,
			PageRequest.of(pageable.getPageNumber()-1, pageable.getPageSize()));

		return getAdminBoardThumbnailPages(spots, SPOT);
	}

	public Page<AdminBoardThumbnail> getAdminAccommodationsByArea(Area area, Pageable pageable) {
		Page<AdminAccommodation> accommodations = adminAccommodationRepository.findAllByArea(area,
			PageRequest.of(pageable.getPageNumber()-1, pageable.getPageSize()));

		return getAdminBoardThumbnailPages(accommodations, ACCOMMODATION);
	}

	public Page<AdminBoardThumbnail> getAdminRestaurantsByArea(Area area, Pageable pageable) {
		Page<AdminRestaurant> restaurants = adminRestaurantRepository.findAllByArea(area,
			PageRequest.of(pageable.getPageNumber()-1, pageable.getPageSize()));

		return getAdminBoardThumbnailPages(restaurants, RESTAURANT);
	}

	public AdminBoardDetail getSpotDetail(String boardId) {
		Info info = adminSpotRepository.findById(boardId)
			.orElseThrow(() -> new BoardNotFoundException(ErrorCode.BOARD_NOT_FOUND));

		return getAdminBoardDetail(info, getImages(info));
	}

	public AdminBoardDetail getAccommodationDetail(String boardId) {
		Info info = adminAccommodationRepository.findById(boardId)
			.orElseThrow(() -> new BoardNotFoundException(ErrorCode.BOARD_NOT_FOUND));

		return getAdminBoardDetail(info, getImages(info));
	}

	public AdminBoardDetail getRestaurantDetail(String boardId) {
		Info info = adminRestaurantRepository.findById(boardId)
			.orElseThrow(() -> new BoardNotFoundException(ErrorCode.BOARD_NOT_FOUND));

		return getAdminBoardDetail(info, getImages(info));
	}

	private List<String> getImages(Info info) {
		return adminBoardImageRepository.findByContentId(info.getContentid())
			.map(AdminBoardImage::getOriginalImageUrls)
			.orElse(emptyList());
	}
}
