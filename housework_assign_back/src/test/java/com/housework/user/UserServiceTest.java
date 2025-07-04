package com.housework.user;


import static org.assertj.core.api.Assertions.assertThat;

import com.housework.auth.dto.KakaoUserDto;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@SpringBootTest
class UserServiceTest {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserService userService;

    @DisplayName("KakaoUser정보의 kakaoId를 이용해 DB조회한다")
    @Test
    public  void  createUserIfNotPresentByKakaoId조회() throws Exception {
        //given
        User user1=createUser(1L,301L);
        User user2=createUser(2L,302L);
        User user3=createUser(3L,303L);
        userRepository.saveAll(List.of(user1,user2,user3));

        KakaoUserDto kakaoUserDto= KakaoUserDto.builder()
            .id(301L).build();

        //when
        Long userId = userService.createUserIfNotPresentByKakaoId(kakaoUserDto);

        //이걸로도 잘 한건데..
        // userService메소드가 이름이 맘에 안드네.
        // USERDTO를 만들건아니어서 user를 return하기싫어서
        // long return하는 상황이니까..
        // 이름 변경해보자!!


        //then
        assertThat(userId).isEqualTo(user1.getId());

    }


    @DisplayName("KakaoUser정보의 kakaoId를 이용해 DB조회 후 없으면 User를 생성한다.")
    @Test
    public  void  createUserIfNotPresentByKakaoId생성() throws Exception {
        //given

        //when

        //then

    }


    private  User createUser(Long id , Long kakaoId){
        return User.builder()
            .id(id)
            .email("email템프")
            .refreshToken("temp refreshToekn")
            .username("kakao"+kakaoId).build();
    }

}