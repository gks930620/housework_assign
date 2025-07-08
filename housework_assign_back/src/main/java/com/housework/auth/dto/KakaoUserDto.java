package com.housework.auth.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;


/**
 *  리소스 서버로부터 받은 카카오 유저 정보들 (로그인 이후)
 */
@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class KakaoUserDto {
    private Long id;
    private Properties properties;
    @JsonProperty( "kakao_account")
    private KakaoAccount kakaoAccount;

    @Getter
    @NoArgsConstructor
    public static class Properties {
        private String nickname;
    }

    @Getter
    @NoArgsConstructor
    public static class KakaoAccount {
        private String email;
    }
}
