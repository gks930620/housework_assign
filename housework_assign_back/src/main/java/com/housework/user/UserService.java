package com.housework.user;

import com.housework.auth.OAuth2Provider;
import com.housework.user.dto.UserDto;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;


    @Transactional(readOnly = false)
    public Long saveUserFromOauth2(String accessToken, String provider) {
            OAuth2Provider oAuth2Provider = OAuth2Provider.from(provider);
            UserDto userDto =oAuth2Provider.getUserFromAccessToken(accessToken);
            return userRepository.findByUsername(userDto.getUsername())
                .orElseGet(() -> {
                    // 신규 사용자 저장
                    User newUser= User.fromDto(userDto);
                    return userRepository.save(newUser);
                }).getId();
    }



    @Transactional(readOnly = false)
    public boolean compareRefreshToken(Long userId, String refreshToken) {
        User user = userRepository.findById(userId).orElseThrow(EntityNotFoundException::new);
        return user.getRefreshToken().equals(refreshToken);
    }


    @Transactional(readOnly = false)
    public void changeRefreshToken(Long id, String newRefreshToken) {
        userRepository.findById(id)
            .ifPresent(user -> user.setRefreshToken(newRefreshToken));
    }


}
