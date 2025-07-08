package com.housework.user;

import com.housework.auth.OAuth2Provider;
import com.housework.user.dto.UserDto;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private OAuth2Provider provider;


    @Column(unique = true, nullable = false)
    private String username;

    private String email;

    private String nickname;

    private LocalDateTime joinedAt;

    @Setter
    @Column(length = 1000)
    private String refreshToken;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"))
    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    private Set<UserRole> roles = new HashSet<>();


    public static User fromDto(UserDto dto) {
        return User.builder()
            .id(dto.getId())
            .provider(dto.getProvider())
            .username(dto.getUsername())
            .email(dto.getEmail())
            .nickname(dto.getNickname())
            .joinedAt(dto.getJoinedAt())
            .refreshToken(dto.getRefreshToken())
            .build();
    }


}
