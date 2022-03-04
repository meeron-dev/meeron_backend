package com.cmc.meeron.auth.application.port.in.request;

import com.cmc.meeron.auth.adapter.in.request.LoginRequest;
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

    public static LoginRequestDto of(LoginRequest loginRequest) {
        return LoginRequestDto.builder()
                .email(loginRequest.getEmail())
                .nickname(loginRequest.getNickname())
                .provider(loginRequest.getProvider().toUpperCase())
                .profileImageUrl(loginRequest.getProfileImageUrl())
                .build();
    }
}
