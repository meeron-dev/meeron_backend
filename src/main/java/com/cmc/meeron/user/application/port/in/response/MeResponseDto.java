package com.cmc.meeron.user.application.port.in.response;

import com.cmc.meeron.user.domain.User;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MeResponseDto {

    private Long userId;
    private String loginEmail;
    private String name;
    private String profileImageUrl;

    public static MeResponseDto fromUser(User user) {
        return MeResponseDto.builder()
                .userId(user.getId())
                .loginEmail(user.getEmail())
                .name(user.getName())
                .profileImageUrl(user.getProfileImageUrl())
                .build();
    }
}
