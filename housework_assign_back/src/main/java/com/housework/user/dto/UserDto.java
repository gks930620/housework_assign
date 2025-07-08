package com.housework.user.dto;

import com.housework.auth.OAuth2Provider;
import com.housework.auth.dto.GoogleUserDto;
import com.housework.auth.dto.KakaoUserDto;
import com.housework.user.User;
import com.housework.user.UserRole;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDto {
    private Long id;
    private OAuth2Provider provider;
    private String username;
    private String email;
    private String nickname;
    private LocalDateTime joinedAt;
    private String refreshToken;
    private Set<UserRole> roles = new HashSet<>();

    public static UserDto fromUser(User user) {
        return UserDto.builder()
            .id(user.getId())
            .provider(user.getProvider())
            .username(user.getUsername())
            .email(user.getEmail())
            .nickname(user.getNickname())
            .joinedAt(user.getJoinedAt())
            .refreshToken(user.getRefreshToken())
            .build();
    }

    public static UserDto createUserFromKakao(KakaoUserDto kakaoUserDto) {
        UserDto newUser = UserDto.builder()
            .provider(OAuth2Provider.KAKAO)
            .username(OAuth2Provider.KAKAO.getProviderName() + kakaoUserDto.getId())
            .nickname(kakaoUserDto.getProperties().getNickname())
            .email(kakaoUserDto.getKakaoAccount().getEmail())
            .joinedAt(LocalDateTime.now())
            .roles(Set.of(UserRole.ROLE_USER))
            .build();
        return newUser;
    }

    public static UserDto createUserFromGoogle(GoogleUserDto googleUserDto) {
        UserDto newUser = UserDto.builder()
            .provider(OAuth2Provider.GOOGLE)
            .username(OAuth2Provider.GOOGLE.getProviderName() + googleUserDto.getSub())
            .email(googleUserDto.getEmail())
            .nickname(googleUserDto.getName())
            .roles(Set.of(UserRole.ROLE_USER))
            .build();
        return newUser;
    }


}
