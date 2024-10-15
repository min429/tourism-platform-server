package com.min429.tourism_platform_server.common.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.min429.tourism_platform_server.common.controller.dto.user.LogInRequest;
import com.min429.tourism_platform_server.common.controller.dto.user.LogInResponse;
import com.min429.tourism_platform_server.common.controller.dto.user.SignUpRequest;
import com.min429.tourism_platform_server.common.service.user.UserService;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("api/users")
@RequiredArgsConstructor
public class UserController {

	private final UserService userService;

	@PostMapping("/signup")
	public void signup(@RequestBody SignUpRequest request) {
		userService.save(request);
	}

	@PostMapping("/login")
	public ResponseEntity<LogInResponse> login(@RequestBody LogInRequest request) {
		return ResponseEntity.ok(userService.login(request));
	}

	@DeleteMapping("/signout")
	public void signout(@RequestBody String email) {
		userService.delete(email);
	}
}
