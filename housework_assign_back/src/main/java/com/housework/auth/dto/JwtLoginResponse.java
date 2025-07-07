package com.housework.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;



@Schema(description = "JWT 로그인 응답 DTO")
@Getter
@AllArgsConstructor
public class JwtLoginResponse {
    @Schema(description = "내 서버 access_token. 카카오 리소스서버에 요청하는 access_token랑 별개", example = "JWT토큰")
    private String accessToken;
    @Schema(description = "refresh_token", example = "JWT토큰값")
    private String refreshToken;
}
