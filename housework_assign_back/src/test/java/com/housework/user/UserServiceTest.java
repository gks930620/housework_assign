package com.housework.user;


import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@SpringBootTest
class UserServiceTest {

//    @Autowired
//    private UserRepository userRepository;
//    @Autowired
//    private UserService userService;
//
//    @AfterEach
//    void dbClear(){
//        userRepository.deleteAllInBatch();
//    }
//
//
//
//    @DisplayName("KakaoUser정보의 kakaoId를 이용해 DB조회한다")
//    @Test
//    public  void  createUserIfNotPresentByKakaoId조회() throws Exception {
//        //given
//        User user1=createUser(1L,301L);
//        User user2=createUser(2L,302L);
//        User user3=createUser(3L,303L);
//        userRepository.saveAll(List.of(user1,user2,user3));
//        KakaoUserDto kakaoUserDto= KakaoUserDto.builder()
//            .id(301L).build();
//        //when
//        Long userId = userService.createUserIfNotPresentByKakaoId(kakaoUserDto);
//        //then
//        assertThat(userId).isEqualTo(user1.getId());
//    }
//
//    @DisplayName("KakaoUser정보의 kakaoId를 이용해 DB조회 후 없으면 User를 생성한다.")
//    @Test
//    public  void  createUserIfNotPresentByKakaoId생성() throws Exception {
//        //given
//        KakaoUserDto kakaoUserDto= KakaoUserDto.builder()
//            .id(301L).build();
//        //when
//        Long userId = userService.createUserIfNotPresentByKakaoId(kakaoUserDto);
//        List<User> all = userRepository.findAll();
//        User user = userRepository.findById(userId).get();
//        //then
//        assertThat(all).hasSize(1);
//        assertThat(user.getUsername()).isEqualTo("kakao" + kakaoUserDto.getId());
//    }
//
//
//    private  User createUser(Long id , Long kakaoId){
//        return User.builder()
//            .id(id)
//            .email("email템프")
//            .refreshToken("temp refreshToekn")
//            .username("kakao"+kakaoId).build();
//    }

}