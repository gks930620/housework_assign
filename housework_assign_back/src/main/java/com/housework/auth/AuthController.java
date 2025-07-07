package com.housework.auth;

import com.housework.auth.dto.JwtLoginResponse;
import com.housework.auth.jwt.JwtTokenProvider;
import com.housework.user.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "인증", description = "카카오/구글 로그인 및 토큰 재발급 API")
public class AuthController {

    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;

    @Operation(
        summary = "OAuth2 로그인 (카카오/구글)",
        description = "사용자로부터 받은 access token(카카오/구글 로그인용)을 이용해  카카오/구글 리소스서버로부터 사용자 정보를 받아 JWT를 발급합니다."
    )
    @ApiResponses(value = {

    })
    @ApiResponse(
        responseCode = "200",
        description = "JWT 토큰 발급 성공",
        content = @Content(schema = @Schema(implementation = JwtLoginResponse.class))
    )
    @PostMapping("/token-login/{provider}")
    public ResponseEntity<JwtLoginResponse> kakaoLogin(
        @RequestBody @Parameter(description = "OAuth2 Access Token") String accessToken,
        @PathVariable("provider") @Parameter(description = "Provider 이름 (kakao 또는 google)") String provider
    ) {
        Long userId = userService.saveUserFromOauth2(accessToken, provider);

        String newAccessToken = jwtTokenProvider.generateAccessToken(userId);
        String newRefreshToken = jwtTokenProvider.generateRefreshToken(userId);

        return ResponseEntity.ok(new JwtLoginResponse(newAccessToken, newRefreshToken));
    }

    @Operation(
        summary = "Access Token 재발급",
        description = "만료된 Access Token을 Refresh Token으로 재발급합니다."
    )
    @ApiResponses(
        value = {
            @ApiResponse(
                responseCode = "200",
                description = "재발급 성공",
                content = @Content(schema = @Schema(implementation = JwtLoginResponse.class))
            ),
            @ApiResponse(responseCode = "401", description = "Refresh Token 유효성 실패" ,  content = @Content())
        }
    )
    @PostMapping("/reissue")
    public ResponseEntity<JwtLoginResponse> reissueToken(
        @RequestBody @Parameter(description = "Refresh Token") String refreshToken
    ) {
        if (!jwtTokenProvider.validateToken(refreshToken)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Long userId = jwtTokenProvider.getUserIdFromToken(refreshToken);
        boolean valid = userService.compareRefreshToken(userId, refreshToken);
        if (valid) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String newAccessToken = jwtTokenProvider.generateAccessToken(userId);
        String newRefreshToken = jwtTokenProvider.generateRefreshToken(userId);
        userService.changeRefreshToken(userId, newRefreshToken);

        return ResponseEntity.ok(new JwtLoginResponse(newAccessToken, newRefreshToken));
    }
}
