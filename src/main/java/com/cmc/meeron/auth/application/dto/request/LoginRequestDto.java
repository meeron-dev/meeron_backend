package com.cmc.meeron.auth.application.dto.request;

import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class LoginRequestDto {

    private String email;
    private String name;
    private String profileImageUrl;
    private String provider;
}
