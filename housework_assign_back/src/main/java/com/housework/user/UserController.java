package com.housework.user;

import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    // 내 정보 보기
    @GetMapping("/me")
    public ResponseEntity<?> getMyInfo(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(Map.of(
            "id", user.getId(),
            "nickname", user.getNickname(),
            "email", user.getEmail()
        ));
    }


}
