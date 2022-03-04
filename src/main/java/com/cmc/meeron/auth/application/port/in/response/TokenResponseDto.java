package com.cmc.meeron.auth.application.port.in.response;

import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class TokenResponseDto {

    private final String type = "Bearer";
    private String accessToken;
    private String refreshToken;

    public static TokenResponseDto of(String accessToken, String refreshToken) {
        return TokenResponseDto.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }
}
