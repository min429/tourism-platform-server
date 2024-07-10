package com.min429.tourism_platform_server.controller.dto.user;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LogInRequest {
    private String email;
    private String password;
}
