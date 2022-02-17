package com.cmc.meeron.auth.presentation.dto.request;

import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
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

    @NotNull(message = "성함을 2 ~ 5자 내외로 입력해주세요.")
    @Length(min = 2, max = 5, message = "성함을 2 ~ 5자 내외로 입력해주세요.")
    private String name;
    private String profileImageUrl;

    @NotBlank(message = "소셜 로그인 제공자를 입력해주세요.")
    private String provider;
}
