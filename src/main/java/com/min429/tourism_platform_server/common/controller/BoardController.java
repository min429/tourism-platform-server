package com.min429.tourism_platform_server.common.controller;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.min429.tourism_platform_server.admin.controller.dto.board.AdminBoardDetail;
import com.min429.tourism_platform_server.common.controller.dto.board.BoardPages;
import com.min429.tourism_platform_server.common.domain.board.Area;
import com.min429.tourism_platform_server.common.domain.board.Type;
import com.min429.tourism_platform_server.common.service.board.BoardService;
import com.min429.tourism_platform_server.user.controller.dto.board.UserBoardDetail;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/boards")
public class BoardController {

	@Value("${naver.map.client-id}")
	private String naverClientId;

	private final BoardService boardService;

	@GetMapping("/thumbnail/{type}")
	public String home(
		@PathVariable("type") Type type,
		@RequestParam("area") Area area,
		@Qualifier("admin") @PageableDefault(size = 10, page = 1) Pageable adminPageable,
		@Qualifier("user") @PageableDefault(size = 10, page = 1) Pageable userPageable,
		Model model)
	{
		BoardPages boardThumbnailPages = boardService.getBoardThumbnailPages(area, type, adminPageable, userPageable);

		model.addAttribute("adminBoardThumbnails", boardThumbnailPages.adminBoards().getContent());
		model.addAttribute("userBoardThumbnails", boardThumbnailPages.userBoards().getContent());

		return "board";
	}

	@GetMapping("/detail/{type}")
	public String detail(
		@PathVariable("type") Type type,
		@RequestParam("area") Area area,
		@RequestParam("boardId") String boardId,
		@RequestParam("isUserBoard") boolean isUserBoard,
		Model model)
	{
		if (isUserBoard) {
			UserBoardDetail boardDetail = boardService.getUserBoardDetail(type, boardId);
			model.addAttribute("boardDetail", boardDetail);
		} else {
			AdminBoardDetail boardDetail = boardService.getAdminBoardDetail(type, boardId);
			model.addAttribute("boardDetail", boardDetail);
		}

		return "board-detail";
	}

	@GetMapping("/new")
	public String newBoard(@RequestParam("area") Area area, Model model) {
		model.addAttribute("area", area);
		model.addAttribute("clientId", naverClientId);

		return "board-new";
	}
}
