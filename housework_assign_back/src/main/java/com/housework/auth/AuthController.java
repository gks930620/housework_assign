package com.housework.auth;

import com.housework.auth.dto.JwtLoginResponse;
import com.housework.auth.dto.KakaoTokenRequest;
import com.housework.auth.dto.KakaoUserDto;
import com.housework.auth.dto.TokenReissueRequest;
import com.housework.auth.jwt.JwtTokenProvider;
import com.housework.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final KakaoAuthService kakaoAuthService;
    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;


    //첫 로그인 -> accessToken을 받아서 카카오 '리소스'서버 접근 
    @PostMapping("/kakao/token-login")
    public ResponseEntity<JwtLoginResponse> kakaoLogin(@RequestBody KakaoTokenRequest request) {
        // 1. access_token → 카카오 사용자 정보 조회
        KakaoUserDto kakaoUser = kakaoAuthService.getUserInfo(request.getAccessToken());
        // 2. 우리 서비스 User 조회.  없으면 저장
        Long userId = userService.createUserIfNotPresentByKakaoId(kakaoUser);
        // 토큰 2종 생성
        String accessToken = jwtTokenProvider.generateAccessToken(userId);
        String refreshToken = jwtTokenProvider.generateRefreshToken(userId);

        // 4. 응답
        return ResponseEntity.ok(new JwtLoginResponse(accessToken,refreshToken));
    }



    @PostMapping("/reissue")
    public ResponseEntity<JwtLoginResponse> reissueToken(@RequestBody TokenReissueRequest request) {
        String refreshToken = request.getRefreshToken();

        // 1. refreshToken 유효성 검사
        if (!jwtTokenProvider.validateToken(refreshToken)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build(); // 401 Unauthorized
        }

        // 2. 토큰에서 사용자 ID 추출
        Long userId = jwtTokenProvider.getUserIdFromToken(refreshToken);
        

        // 3. 사용자 조회 + 현재 저장된 refreshToken과 비교
        userService.compareRefreshToken(userId,refreshToken);
        //다르면 에러.  에러보내는게 맞음. 보내고 끝!

        // 4. 새 accessToken 및 refreshToken 생성 (회전 전략)
        String newAccessToken = jwtTokenProvider.generateAccessToken(userId);
        String newRefreshToken = jwtTokenProvider.generateRefreshToken(userId);


        // 5. 사용자 엔티티에 새 refreshToken 저장 (기존 것은 폐기됨)
        userService.changeRefreshToken(userId,newRefreshToken);

        // 6. 응답 반환
        return ResponseEntity.ok(new JwtLoginResponse(newAccessToken, newRefreshToken));
    }



}
