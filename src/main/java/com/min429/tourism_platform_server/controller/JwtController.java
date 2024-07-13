package com.min429.tourism_platform_server.controller;

import com.min429.tourism_platform_server.controller.dto.user.LogInResponse;
import com.min429.tourism_platform_server.service.JwtService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("api/tokens")
public class JwtController {

    private final JwtService jwtService;

    @PostMapping("/create")
    public ResponseEntity<LogInResponse> refreshTokens(@RequestHeader("Authorization") String authorizationHeader) {
        return ResponseEntity.status(HttpStatus.CREATED).
                body(jwtService.refreshTokens(authorizationHeader));
    }
}
