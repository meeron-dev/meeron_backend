package com.cmc.meeron.workspace.application.port.in.response;

import com.cmc.meeron.user.domain.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponseDto {

    private Long userId;
    private String loginEmail;
    private String name;
    private String profileImageUrl;

    public static UserResponseDto fromEntity(User user) {
        return UserResponseDto.builder()
                .userId(user.getId())
                .loginEmail(user.getEmail())
                .name(user.getName())
                .profileImageUrl(user.getProfileImageUrl())
                .build();
    }
}
