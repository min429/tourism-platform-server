package com.min429.tourism_platform_server.user.service;

import static com.min429.tourism_platform_server.common.domain.board.Info.*;
import static com.min429.tourism_platform_server.common.domain.board.Type.*;
import static java.util.Collections.*;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.min429.tourism_platform_server.common.controller.dto.board.BoardPages;
import com.min429.tourism_platform_server.common.domain.board.Area;
import com.min429.tourism_platform_server.common.domain.board.Info;
import com.min429.tourism_platform_server.common.domain.board.Type;
import com.min429.tourism_platform_server.common.exception.board.BoardNotFoundException;
import com.min429.tourism_platform_server.common.exception.common.IllegalArgumentException;
import com.min429.tourism_platform_server.common.exception.response.ErrorCode;
import com.min429.tourism_platform_server.common.service.firebase.FirebaseService;
import com.min429.tourism_platform_server.user.controller.dto.board.CreateBoardRequest;
import com.min429.tourism_platform_server.user.controller.dto.board.UpdateBoardRequest;
import com.min429.tourism_platform_server.user.controller.dto.board.UserBoardDetail;
import com.min429.tourism_platform_server.user.controller.dto.board.UserBoardThumbnail;
import com.min429.tourism_platform_server.user.domain.UserAccommodation;
import com.min429.tourism_platform_server.user.domain.UserBoardImage;
import com.min429.tourism_platform_server.user.domain.UserRestaurant;
import com.min429.tourism_platform_server.user.domain.UserSpot;
import com.min429.tourism_platform_server.user.repository.UserAccommodationRepository;
import com.min429.tourism_platform_server.user.repository.UserBoardImageRepository;
import com.min429.tourism_platform_server.user.repository.UserRestaurantRepository;
import com.min429.tourism_platform_server.user.repository.UserSpotRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class UserBoardService {
	private final UserSpotRepository userSpotRepository;
	private final UserAccommodationRepository userAccommodationRepository;
	private final UserRestaurantRepository userRestaurantRepository;
	private final UserBoardImageRepository userBoardImageRepository;
	private final FirebaseService firebaseService;

	public void update(Type type, Area area, UpdateBoardRequest request, String boardId) {
		delete(type, boardId);
		create(type, area, request, boardId);
	}

	public void delete(Type type, String boardId) {
		switch (type) {
			case SPOT:
				userSpotRepository.deleteById(boardId);
				break;
			case ACCOMMODATION:
				userAccommodationRepository.deleteById(boardId);
				break;
			case RESTAURANT:
				userRestaurantRepository.deleteById(boardId);
				break;
			default:
				throw new IllegalArgumentException(ErrorCode.BAD_REQUEST);
		}
		userBoardImageRepository.deleteByBoardId(boardId);
	}

	public BoardPages getBoardThumbnailPages(Type type, Area area, String nickname, Pageable pageable) {
		switch (type) {
			case SPOT:
				return getSpotPages(area, nickname, pageable);
			case ACCOMMODATION:
				return getAccommodationPages(area, nickname, pageable);
			case RESTAURANT:
				return getRestaurantPages(area, nickname, pageable);
			default:
				throw new IllegalArgumentException(ErrorCode.BAD_REQUEST);
		}
	}

	public BoardPages getSpotPages(Area area, String nickname, Pageable pageable) {
		Page<UserBoardThumbnail> userSpotThumbnails = getUserSpotsBy(area, nickname, pageable);

		return getBoardPages(userSpotThumbnails);
	}

	public BoardPages getAccommodationPages(Area area, String nickname, Pageable pageable) {
		Page<UserBoardThumbnail> userAccommodationThumbnails = getUserAccommodationsBy(area, nickname, pageable);

		return getBoardPages(userAccommodationThumbnails);
	}

	public BoardPages getRestaurantPages(Area area, String nickname, Pageable pageable) {
		Page<UserBoardThumbnail> userRestaurantThumbnails = getUserRestaurantsBy(area, nickname, pageable);

		return getBoardPages(userRestaurantThumbnails);
	}

	public Page<UserBoardThumbnail> getUserSpotsBy(Area area, String nickname, Pageable pageable) {
		Page<UserSpot> spots = userSpotRepository.findAllBy(area, nickname,
			PageRequest.of(pageable.getPageNumber() - 1, pageable.getPageSize()));

		return getUserBoardThumbnailPages(spots, SPOT);
	}

	public Page<UserBoardThumbnail> getUserAccommodationsBy(Area area, String nickname, Pageable pageable) {
		Page<UserAccommodation> accommodations = userAccommodationRepository.findAllBy(area, nickname,
			PageRequest.of(pageable.getPageNumber() - 1, pageable.getPageSize()));

		return getUserBoardThumbnailPages(accommodations, ACCOMMODATION);
	}

	public Page<UserBoardThumbnail> getUserRestaurantsBy(Area area, String nickname, Pageable pageable) {
		Page<UserRestaurant> restaurants = userRestaurantRepository.findAllBy(area, nickname,
			PageRequest.of(pageable.getPageNumber() - 1, pageable.getPageSize()));

		return getUserBoardThumbnailPages(restaurants, RESTAURANT);
	}

	private BoardPages getBoardPages(Page<UserBoardThumbnail> userBoardThumbnails) {
		return BoardPages.builder()
			.userBoards(userBoardThumbnails)
			.build();
	}

	public Page<UserBoardThumbnail> getUserSpotsByArea(Area area, Pageable pageable) {
		Page<UserSpot> spots = userSpotRepository.findAllByArea(area,
			PageRequest.of(pageable.getPageNumber() - 1, pageable.getPageSize()));

		return getUserBoardThumbnailPages(spots, SPOT);
	}

	public Page<UserBoardThumbnail> getUserAccommodationsByArea(Area area, Pageable pageable) {
		Page<UserAccommodation> accommodations = userAccommodationRepository.findAllByArea(area,
			PageRequest.of(pageable.getPageNumber() - 1, pageable.getPageSize()));

		return getUserBoardThumbnailPages(accommodations, ACCOMMODATION);
	}

	public Page<UserBoardThumbnail> getUserRestaurantsByArea(Area area, Pageable pageable) {
		Page<UserRestaurant> restaurants = userRestaurantRepository.findAllByArea(area,
			PageRequest.of(pageable.getPageNumber() - 1, pageable.getPageSize()));

		return getUserBoardThumbnailPages(restaurants, RESTAURANT);
	}

	public UserBoardDetail getSpotDetail(String boardId) {
		Info info = userSpotRepository.findById(boardId)
			.orElseThrow(() -> new BoardNotFoundException(ErrorCode.BOARD_NOT_FOUND));

		return getUserBoardDetail(info, getImages(info));
	}

	public UserBoardDetail getAccommodationDetail(String boardId) {
		Info info = userAccommodationRepository.findById(boardId)
			.orElseThrow(() -> new BoardNotFoundException(ErrorCode.BOARD_NOT_FOUND));

		return getUserBoardDetail(info, getImages(info));
	}

	public UserBoardDetail getRestaurantDetail(String boardId) {
		Info info = userRestaurantRepository.findById(boardId)
			.orElseThrow(() -> new BoardNotFoundException(ErrorCode.BOARD_NOT_FOUND));

		return getUserBoardDetail(info, getImages(info));
	}

	public void create(Type type, Area area, UpdateBoardRequest request, String id) {
		String boardId = id;

		switch (type) {
			case SPOT:
				UserSpot userSpot = userSpotRepository.save(UserSpot.builder()
					.id(boardId)
					.title(request.title())
					.overview(request.description())
					.area(area)
					.addr1(request.address())
					.tags(request.tags())
					.build());
				break;
			case ACCOMMODATION:
				UserAccommodation userAccommodation = userAccommodationRepository.save(UserAccommodation.builder()
					.id(boardId)
					.title(request.title())
					.overview(request.description())
					.area(area)
					.addr1(request.address())
					.tags(request.tags())
					.build());
				break;
			case RESTAURANT:
				UserRestaurant userRestaurant = userRestaurantRepository.save(UserRestaurant.builder()
					.id(boardId)
					.title(request.title())
					.overview(request.description())
					.area(area)
					.addr1(request.address())
					.tags(request.tags())
					.build());
				break;
			default:
				throw new IllegalArgumentException(ErrorCode.BAD_REQUEST);
		}

		saveBoardImage(boardId, request.images(), request.imageUrls());
	}

	public void create(Type type, Area area, CreateBoardRequest request) {
		String boardId;

		switch (type) {
			case SPOT:
				UserSpot userSpot = userSpotRepository.save(UserSpot.builder()
					.title(request.title())
					.overview(request.description())
					.area(area)
					.addr1(request.address())
					.tags(request.tags())
					.build());
				boardId = userSpot.getId();
				break;
			case ACCOMMODATION:
				UserAccommodation userAccommodation = userAccommodationRepository.save(UserAccommodation.builder()
					.title(request.title())
					.overview(request.description())
					.area(area)
					.addr1(request.address())
					.tags(request.tags())
					.build());
				boardId = userAccommodation.getId();
				break;
			case RESTAURANT:
				UserRestaurant userRestaurant = userRestaurantRepository.save(UserRestaurant.builder()
					.title(request.title())
					.overview(request.description())
					.area(area)
					.addr1(request.address())
					.tags(request.tags())
					.build());
				boardId = userRestaurant.getId();
				break;
			default:
				throw new IllegalArgumentException(ErrorCode.BAD_REQUEST);
		}

		saveBoardImage(boardId, request.images());
	}

	private void saveBoardImage(String boardId, List<MultipartFile> images, List<String> urls) {
		if (images != null) {
			List<String> imageUrls = new ArrayList<>();

			for (MultipartFile file : images) {
				String fileName = file.getOriginalFilename();
				String imageUrl = firebaseService.uploadFiles(file, fileName);
				imageUrls.add(imageUrl);
			}

			imageUrls.addAll(urls);

			UserBoardImage boardImage = UserBoardImage.builder()
				.boardId(boardId)
				.originalImageUrls(imageUrls)
				.smallImageUrls(imageUrls)
				.build();

			userBoardImageRepository.save(boardImage);
		}
	}

	private void saveBoardImage(String boardId, List<MultipartFile> images) {
		if (images != null) {
			List<String> imageUrls = new ArrayList<>();

			for (MultipartFile file : images) {
				String fileName = file.getOriginalFilename();
				String imageUrl = firebaseService.uploadFiles(file, fileName);
				imageUrls.add(imageUrl);
			}

			UserBoardImage boardImage = UserBoardImage.builder()
				.boardId(boardId)
				.originalImageUrls(imageUrls)
				.smallImageUrls(imageUrls)
				.build();

			userBoardImageRepository.save(boardImage);
		}
	}

	private List<String> getImages(Info info) {
		return userBoardImageRepository.findByBoardId(info.getId())
			.map(UserBoardImage::getOriginalImageUrls)
			.orElse(emptyList());
	}
}
