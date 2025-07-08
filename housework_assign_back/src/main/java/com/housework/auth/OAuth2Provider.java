package com.housework.auth;

import com.housework.auth.dto.GoogleUserDto;
import com.housework.auth.dto.KakaoUserDto;
import com.housework.user.dto.UserDto;
import java.util.Arrays;
import org.springframework.web.reactive.function.client.WebClient;

public enum OAuth2Provider {
    KAKAO("kakao") {
        @Override
        public UserDto getUserFromAccessToken(String accessToken) {
            KakaoUserDto dto = WebClient.create("https://kapi.kakao.com")
                .get()
                .uri("/v2/user/me")
                .header("Authorization", "Bearer " + accessToken)
                .retrieve()
                .bodyToMono(KakaoUserDto.class)
                .block();
            return UserDto.createUserFromKakao(dto);
        }
    },

    GOOGLE("google") {
        @Override
        public UserDto getUserFromAccessToken(String accessToken) {
            GoogleUserDto dto = WebClient.create()
                .get()
                .uri("https://www.googleapis.com/oauth2/v3/userinfo")
                .headers(h -> h.setBearerAuth(accessToken))
                .retrieve()
                .bodyToMono(GoogleUserDto.class)
                .block();
            //user를 만드는게 아니라   DB에 있냐 없냐 조회하고 해야된느데..
            return UserDto.createUserFromGoogle(dto);
        }
    };

    private final String providerName;

    public String getProviderName() {
        return providerName;
    }

    OAuth2Provider(String providerName) {
        this.providerName = providerName;
    }

    public abstract UserDto getUserFromAccessToken(String accessToken);


    public static OAuth2Provider from(String providerName) {
        return Arrays.stream(values())
            .filter(p -> p.getProviderName().equalsIgnoreCase(providerName))
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException("Unknown provider: " + providerName));
    }

}
