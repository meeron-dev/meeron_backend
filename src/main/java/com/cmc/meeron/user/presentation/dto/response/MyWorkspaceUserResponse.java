package com.cmc.meeron.user.presentation.dto.response;

import com.cmc.meeron.user.application.dto.response.MyWorkspaceUserResponseDto;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MyWorkspaceUserResponse {

    private Long workspaceUserId;
    private Long workspaceId;
    private boolean isWorkspaceAdmin;
    private String nickname;
    private String profileImageUrl;
    private String position;

    public static MyWorkspaceUserResponse fromWorkspaceUser(MyWorkspaceUserResponseDto myWorkspaceUserResponseDto) {
        return MyWorkspaceUserResponse.builder()
                .workspaceUserId(myWorkspaceUserResponseDto.getWorkspaceUserId())
                .workspaceId(myWorkspaceUserResponseDto.getWorkspaceId())
                .isWorkspaceAdmin(myWorkspaceUserResponseDto.isWorkspaceAdmin())
                .nickname(myWorkspaceUserResponseDto.getNickname())
                .profileImageUrl(myWorkspaceUserResponseDto.getProfileImageUrl())
                .position(myWorkspaceUserResponseDto.getPosition())
                .build();
    }
}
