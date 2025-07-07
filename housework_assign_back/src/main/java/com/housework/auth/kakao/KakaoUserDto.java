package com.housework.auth.kakao;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 *  리소스 서버로부터 받은 카카오 유저 정보들 (로그인 이후)
 */
@Data
@NoArgsConstructor
@Builder
public class KakaoUserDto {
    private Long id;            // 카카오 고유 ID
    private String nickname;    // 사용자 닉네임
    private String email;       // 이메일 (nullable)


    public KakaoUserDto(Long id, String nickname, String email) {
        this.id = id;
        this.nickname = nickname;
        this.email = email;
    }
}
