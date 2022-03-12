package com.cmc.meeron.user.adapter.in.response;

import com.cmc.meeron.user.application.port.in.response.MyWorkspaceUserResponseDto;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WorkspaceUserResponse {

    private Long workspaceUserId;
    private Long workspaceId;
    private boolean isWorkspaceAdmin;
    private String nickname;
    private String profileImageUrl;
    private String position;

    public static WorkspaceUserResponse fromWorkspaceUser(MyWorkspaceUserResponseDto myWorkspaceUserResponseDto) {
        return WorkspaceUserResponse.builder()
                .workspaceUserId(myWorkspaceUserResponseDto.getWorkspaceUserId())
                .workspaceId(myWorkspaceUserResponseDto.getWorkspaceId())
                .isWorkspaceAdmin(myWorkspaceUserResponseDto.isWorkspaceAdmin())
                .nickname(myWorkspaceUserResponseDto.getNickname())
                .profileImageUrl(myWorkspaceUserResponseDto.getProfileImageUrl())
                .position(myWorkspaceUserResponseDto.getPosition())
                .build();
    }
}
