package com.cmc.meeron.auth.presentation.dto.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TokenResponse {

    private String type;
    private String accessToken;
    private String refreshToken;
}
