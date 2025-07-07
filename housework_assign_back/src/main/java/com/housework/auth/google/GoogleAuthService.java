package com.housework.auth.google;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@RequiredArgsConstructor
public class GoogleAuthService {

    private final WebClient webClient = WebClient.create();

    public GoogleUserDto getUserInfo(String accessToken) {
        return webClient.get()
            .uri("https://www.googleapis.com/oauth2/v3/userinfo")
            .headers(h -> h.setBearerAuth(accessToken))
            .retrieve()
            .bodyToMono(GoogleUserDto.class)
            .block();
    }
}
