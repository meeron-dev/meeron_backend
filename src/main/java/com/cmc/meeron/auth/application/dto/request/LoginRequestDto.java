package com.cmc.meeron.auth.application.dto.request;

import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class LoginRequestDto {

    // TODO: 2022/02/19 kobeomseok95 VO로 개선해볼까?
    private String email;
    private String nickname;
    private String profileImageUrl;
    private String provider;
}
