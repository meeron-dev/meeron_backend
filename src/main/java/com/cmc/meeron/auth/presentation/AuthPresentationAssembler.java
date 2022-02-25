package com.cmc.meeron.auth.presentation;

import com.cmc.meeron.auth.application.dto.request.LoginRequestDto;
import com.cmc.meeron.auth.presentation.dto.request.LoginRequest;

class AuthPresentationAssembler {

    static LoginRequestDto toLoginRequestDto(LoginRequest loginRequest) {
        return LoginRequestDto.builder()
                .email(loginRequest.getEmail())
                .nickname(loginRequest.getNickname() == null ? "" : loginRequest.getNickname())
                .profileImageUrl(loginRequest.getProfileImageUrl() == null ? "" : loginRequest.getProfileImageUrl())
                .provider(loginRequest.getProvider().toUpperCase())
                .build();
    }
}
