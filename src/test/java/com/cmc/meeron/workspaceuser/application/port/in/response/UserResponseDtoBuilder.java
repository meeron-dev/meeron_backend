package com.cmc.meeron.workspaceuser.application.port.in.response;

import com.cmc.meeron.workspaceuser.application.port.in.response.UserResponseDto;

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
