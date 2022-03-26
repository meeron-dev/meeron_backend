package com.cmc.meeron.workspace.adapter.in.response;

import com.cmc.meeron.workspace.application.port.in.response.MyWorkspaceUserResponseDto;
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
    private String email;

    public static WorkspaceUserResponse fromWorkspaceUser(MyWorkspaceUserResponseDto myWorkspaceUserResponseDto) {
        return WorkspaceUserResponse.builder()
                .workspaceUserId(myWorkspaceUserResponseDto.getWorkspaceUserId())
                .workspaceId(myWorkspaceUserResponseDto.getWorkspaceId())
                .isWorkspaceAdmin(myWorkspaceUserResponseDto.isWorkspaceAdmin())
                .nickname(myWorkspaceUserResponseDto.getNickname())
                .profileImageUrl(myWorkspaceUserResponseDto.getProfileImageUrl())
                .position(myWorkspaceUserResponseDto.getPosition())
                .email(myWorkspaceUserResponseDto.getEmail())
                .build();
    }
}
