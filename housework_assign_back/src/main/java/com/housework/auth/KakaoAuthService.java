package com.housework.auth;

import com.housework.auth.dto.KakaoUserDto;
import org.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class KakaoAuthService  {

    private final RestTemplate restTemplate = new RestTemplate();

    public KakaoUserDto getUserInfo(String accessToken) {
        // 1. 헤더 설정
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken); // Authorization: Bearer {access_token}
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<?> entity = new HttpEntity<>(headers);

        // 2. 카카오 사용자 정보 요청  (인가코드방식에서는  DefaultOaut2UserService를 만들고 필터를 등록하지만
        // 우리는 securityFilter에 맡기는게 아닌 직접 access_token으로 카카오 '리소스'서버에  카카오사용자정보 요청
        ResponseEntity<String> response = restTemplate.exchange(
            "https://kapi.kakao.com/v2/user/me",
            HttpMethod.GET,
            entity,
            String.class
        );

        if (response.getStatusCode() != HttpStatus.OK) {
            throw new RuntimeException("카카오 사용자 정보 조회 실패");
        }

        // 3. 응답 JSON 파싱
        JSONObject body = new JSONObject(response.getBody());
        KakaoUserDto user = new KakaoUserDto();
        user.setId(body.getLong("id"));
        JSONObject properties = body.optJSONObject("properties");
        if (properties != null) {
            user.setNickname(properties.optString("nickname", ""));
        }
        JSONObject kakaoAccount = body.optJSONObject("kakao_account");
        if (kakaoAccount != null) {
            user.setEmail(kakaoAccount.optString("email", null));
        }
        return user;
    }
}
