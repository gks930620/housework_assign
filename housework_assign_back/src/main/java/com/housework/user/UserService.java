package com.housework.user;

import com.housework.auth.google.GoogleUserDto;
import com.housework.auth.kakao.KakaoUserDto;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public Long createUserIfNotPresentByKakaoId(KakaoUserDto kakaoUserDto) {
        return userRepository.findByUsername("kakao"+kakaoUserDto.getId())
                .orElseGet(() -> {
                    // 신규 사용자 저장
                    User newUser = User.builder()
                        .provider("kakao")
                            .username("kakao" + kakaoUserDto.getId())
                            .nickname(kakaoUserDto.getNickname())
                            .email(kakaoUserDto.getEmail())
                            .joinedAt(LocalDateTime.now())
                            .build();
                    return userRepository.save(newUser);
                }).getId();
    }

    public User findOrCreateUserByGoogle(GoogleUserDto googleUserDto) {
        return userRepository.findByGoogleId(googleUserDto.getSub())
            .orElseGet(() -> {
                User newUser = User.builder()
                    .provider("google")
                    .username("google" + googleUserDto.getSub())
                    .email(googleUserDto.getEmail())
                    .nickname(googleUserDto.getName())
                    .build();
                return userRepository.save(newUser);
            });
    }



    public void compareRefreshToken(Long userId, String refreshToken) {
        userRepository.findById(userId)
            .filter(u -> refreshToken.equals(
                u.getRefreshToken()))  //현재 DB랑 비교해야  내가 가지고 있는 refresh토큰이 맞늦지 확인
            .orElseThrow(() -> new RuntimeException("Invalid refresh token"));
    }


    public void changeRefreshToken(Long id,String newRefreshToken) {
        userRepository.findById(id)
            .ifPresent(user -> user.setRefreshToken(newRefreshToken));
    }







}
