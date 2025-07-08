package com.housework.user;

import com.housework.auth.dto.CustomUserDetails;
import com.housework.user.dto.MyInfoResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    //NOTE : 알고있자. 필터등에 의해 걸려지는 요청도 controller에서 문서화해야된다.

    @Operation(summary = "내 정보 조회", description = "현재 로그인한 사용자의 정보를 조회합니다.")
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "성공적으로 사용자 정보를 반환",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = MyInfoResponse.class)
            )
        ),
        @ApiResponse(responseCode = "401", description = "인증되지 않은 사용자 (토큰 없음 또는 만료됨)" ,content = @Content()),
        @ApiResponse(responseCode = "403", description = "인가되지 않은 접근", content = @Content()) // 권한 로직이 추가되었을 경우
    })
    @GetMapping("/me")
    public ResponseEntity<?> getMyInfo(@AuthenticationPrincipal CustomUserDetails user) {
        return ResponseEntity.ok(new MyInfoResponse(user.getUserDto()));
    }

}



