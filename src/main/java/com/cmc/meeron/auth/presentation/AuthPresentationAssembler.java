package com.cmc.meeron.auth.presentation;

import com.cmc.meeron.auth.application.dto.request.LoginRequestDto;
import com.cmc.meeron.auth.presentation.dto.request.LoginRequest;

public class AuthPresentationAssembler {

    static LoginRequestDto toLoginRequestDto(LoginRequest loginRequest) {
        return LoginRequestDto.builder()
                .email(loginRequest.getEmail())
                .nickname(loginRequest.getNickname().isBlank() ? "" : loginRequest.getNickname())
                .profileImageUrl(loginRequest.getProfileImageUrl().isBlank() ? "" : loginRequest.getProfileImageUrl())
                .provider(loginRequest.getProvider().toUpperCase())
                .build();
    }
}
