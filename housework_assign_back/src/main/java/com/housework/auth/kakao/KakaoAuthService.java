package com.housework.auth.kakao;

import org.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

@Service
public class KakaoAuthService {

    private final WebClient webClient;

    public KakaoAuthService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("https://kapi.kakao.com").build();
    }

    //프론트로부터 받은 access_token(카카오로그인 정보)로   카카오 리소스서버 요청
    public KakaoUserDto getUserInfo(String accessToken) {
        try {
            String response = webClient.get()
                .uri("/v2/user/me")
                .header("Authorization", "Bearer " + accessToken)
                .retrieve()
                .bodyToMono(String.class)
                .block();
            JSONObject body = new JSONObject(response);
            Long kakaoId = body.getLong("id");
            String nickname = body.getJSONObject("properties").optString("nickname", null);
            String email = body.optJSONObject("kakao_account") != null
                ? body.getJSONObject("kakao_account").optString("email", null)
                : null;
            return new KakaoUserDto(kakaoId, nickname, email);
        } catch (WebClientResponseException e) {
            throw new RuntimeException("Failed to get Kakao user info: " + e.getStatusCode(), e);
        }
    }
}
