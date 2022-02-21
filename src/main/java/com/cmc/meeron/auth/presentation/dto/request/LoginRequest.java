package com.cmc.meeron.auth.presentation.dto.request;

import com.cmc.meeron.common.validator.EnumValid;
import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginRequest {

    @NotNull(message = "이메일을 입력해주세요.")
    @Email(message = "이메일 형식으로 입력해주세요.")
    private String email;
    private String nickname;
    private String profileImageUrl;

    @EnumValid(message = "지원하지 않는 소셜 로그인 방식입니다.", enumClass = SocialLoginProvider.class)
    private String provider;

    @Getter
    @AllArgsConstructor
    public enum SocialLoginProvider {

        KAKAO("KAKAO"),
        APPLE("APPLE"),
        ;

        private final String providerValue;
    }
}
