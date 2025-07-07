package com.housework.auth;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class JwtLoginResponse {
    private String accessToken;
    private String refreshToken;
}
