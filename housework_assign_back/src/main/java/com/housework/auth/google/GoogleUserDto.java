package com.housework.auth.google;

import lombok.*;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GoogleUserDto {
    private String sub;       // Google 고유 ID
    private String email;
    private String name;
    private String picture;
}
