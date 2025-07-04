package com.housework.auth.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class KakaoUserDto {
    private Long id;            // 카카오 고유 ID
    private String nickname;    // 사용자 닉네임
    private String email;       // 이메일 (nullable)
}
