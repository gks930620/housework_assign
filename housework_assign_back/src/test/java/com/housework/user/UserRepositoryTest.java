package com.housework.user;


import static org.assertj.core.api.Assertions.assertThat;

import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@DataJpaTest
class UserRepositoryTest {
    @Autowired
    private  UserRepository userRepository;

    @DisplayName("username을 통해 user를 찾는다.")
    @Test
    public  void  findByKakaoId () throws Exception {
        //given
        User user1= createUser(1L,1L);
        User user2= createUser(2L,330L);
        User user3= createUser(3L,313L);
        userRepository.saveAll(List.of(user1, user2, user3));

        //when
        User findUser = userRepository.findByUsername("kakao313")
            .orElseThrow(EntityNotFoundException::new);

        //then
        assertThat(findUser.getUsername()).isEqualTo("kakao313");
        assertThat(findUser.getId()).isEqualTo(3L);
    }


    private  User createUser(Long id , Long kakaoId){
        return User.builder()
            .id(id)
            .email("email템프")
            .refreshToken("temp refreshToekn")
            .username("kakao"+kakaoId).build();
    }

}