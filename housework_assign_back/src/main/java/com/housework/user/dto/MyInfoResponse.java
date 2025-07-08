package com.housework.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Schema(description = "내 정보 응답 DTO")
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class MyInfoResponse {
    @Schema(description = "사용자 ID", example = "1")
    private Long id;

    @Schema(description = "닉네임", example = "홍길동")
    private String nickname;

    @Schema(description = "이메일", example = "hong@naver.com")
    private String email;

    public MyInfoResponse(UserDto user) {
        this.id = user.getId();
        this.nickname = user.getNickname();
        this.email = user.getEmail();
    }



}