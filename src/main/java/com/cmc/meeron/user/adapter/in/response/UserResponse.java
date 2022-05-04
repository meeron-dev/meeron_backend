package com.cmc.meeron.user.adapter.in.response;

import com.cmc.meeron.user.application.port.in.response.UserResponseDto;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponse {

    private Long userId;
    private String loginEmail;
    private String name;
    private String profileImageUrl;

    public static UserResponse from(UserResponseDto userResponseDto) {
        return UserResponse.builder()
                .userId(userResponseDto.getUserId())
                .loginEmail(userResponseDto.getLoginEmail())
                .name(userResponseDto.getName())
                .profileImageUrl(userResponseDto.getProfileImageUrl())
                .build();
    }
}
