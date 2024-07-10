package com.min429.tourism_platform_server.controller;


import com.min429.tourism_platform_server.controller.dto.user.LogInRequest;
import com.min429.tourism_platform_server.controller.dto.user.LogInResponse;
import com.min429.tourism_platform_server.controller.dto.user.SignUpRequest;
import com.min429.tourism_platform_server.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<String> signup(@RequestBody SignUpRequest request) {
        userService.save(request);
        return ResponseEntity.ok().body("회원가입 성공");
    }

    @PostMapping("/login")
    public ResponseEntity<LogInResponse> login(@RequestBody LogInRequest request){
        return ResponseEntity.ok().body(userService.login(request));
    }

    @DeleteMapping("/signout")
    public ResponseEntity<String> signout(@RequestBody String email) {
        userService.delete(email);
        return ResponseEntity.ok().body("회원탈퇴 완료");
    }
}
