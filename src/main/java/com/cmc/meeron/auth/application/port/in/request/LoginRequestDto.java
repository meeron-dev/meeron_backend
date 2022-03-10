package com.cmc.meeron.auth.application.port.in.request;

import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class LoginRequestDto {

    private String email;
    private String nickname;
    private String profileImageUrl;
    private String provider;
}
