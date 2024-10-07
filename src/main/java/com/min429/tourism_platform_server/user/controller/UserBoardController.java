package com.min429.tourism_platform_server.user.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.min429.tourism_platform_server.common.controller.dto.board.BoardPages;
import com.min429.tourism_platform_server.common.domain.User;
import com.min429.tourism_platform_server.common.domain.board.Area;
import com.min429.tourism_platform_server.common.domain.board.Type;
import com.min429.tourism_platform_server.common.service.board.BoardService;
import com.min429.tourism_platform_server.user.controller.dto.board.CreateBoardRequest;
import com.min429.tourism_platform_server.user.controller.dto.board.UpdateBoardRequest;
import com.min429.tourism_platform_server.user.controller.dto.board.UserBoardDetail;
import com.min429.tourism_platform_server.user.service.UserBoardService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/user/boards")
public class UserBoardController {

	@Value("${naver.map.client-id}")
	private String naverClientId;

	private final UserBoardService userBoardService;
	private final BoardService boardService;

	@PostMapping("/new/{type}")
	public ResponseEntity<Void> create(
		@PathVariable("type") Type type,
		@RequestParam("area") Area area,
		@ModelAttribute CreateBoardRequest request) {
		userBoardService.create(type, area, request);

		return ResponseEntity.ok().build();
	}

	@GetMapping("/thumbnail/mine/{type}")
	public String home(
		@PathVariable("type") Type type,
		@RequestParam("area") Area area,
		@AuthenticationPrincipal User user,
		@PageableDefault(size = 10, page = 1) Pageable pageable,
		Model model) {
		// BoardPages boardThumbnailPages = userBoardService.getBoardThumbnailPages(type, area, user.getNickname(), pageable);

		BoardPages boardThumbnailPages = userBoardService.getBoardThumbnailPages(type, area, null, pageable);

		model.addAttribute("myBoardThumbnails", boardThumbnailPages.userBoards().getContent());
		System.out.println("myBoardThumbnails: " + boardThumbnailPages.userBoards().getContent());

		// model.addAttribute("nickname", user.getNickname());
		model.addAttribute("nickname", null);

		return "board-mine";
	}

	@DeleteMapping("/{type}/{id}")
	public ResponseEntity<Void> delete(
		@PathVariable("type") Type type,
		@RequestParam("boardId") String boardId) {
		userBoardService.delete(type, boardId);

		return ResponseEntity.ok().build();
	}

	@PostMapping("update/{type}")
	public ResponseEntity<Void> update(
		@PathVariable("type") Type type,
		@RequestParam("area") Area area,
		@RequestParam("boardId") String boardId,
		@ModelAttribute UpdateBoardRequest request) {
		userBoardService.update(type, area, request, boardId);

		return ResponseEntity.ok().build();
	}

	@GetMapping("/editForm/{type}")
	public String editForm(
		@PathVariable("type") Type type,
		@RequestParam("area") Area area,
		@RequestParam("boardId") String boardId,
		Model model) {
		model.addAttribute("clientId", naverClientId);
		return "board-edit";
	}

	@GetMapping("/content/{type}")
	@ResponseBody
	public ResponseEntity<UserBoardDetail> getContent(
		@PathVariable("type") Type type,
		@RequestParam("boardId") String boardId) {
		return ResponseEntity.ok(boardService.getUserBoardDetail(type, boardId));
	}
}
