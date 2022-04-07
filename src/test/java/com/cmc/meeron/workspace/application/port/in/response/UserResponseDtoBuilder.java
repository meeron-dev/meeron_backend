package com.cmc.meeron.workspace.application.port.in.response;

public class UserResponseDtoBuilder {

    public static UserResponseDto build() {
        return UserResponseDto.builder()
                .userId(1L)
                .loginEmail("login@email.com")
                .name("성함")
                .profileImageUrl("https://test.com")
                .build();
    }
}
