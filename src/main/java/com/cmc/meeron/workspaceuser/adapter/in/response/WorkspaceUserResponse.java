package com.cmc.meeron.workspaceuser.adapter.in.response;

import com.cmc.meeron.workspaceuser.application.port.in.response.WorkspaceUserResponseDto;
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
    private String phone;

    public static WorkspaceUserResponse from(WorkspaceUserResponseDto workspaceUserResponseDto) {
        return WorkspaceUserResponse.builder()
                .workspaceUserId(workspaceUserResponseDto.getWorkspaceUserId())
                .workspaceId(workspaceUserResponseDto.getWorkspaceId())
                .isWorkspaceAdmin(workspaceUserResponseDto.isWorkspaceAdmin())
                .nickname(workspaceUserResponseDto.getNickname())
                .profileImageUrl(workspaceUserResponseDto.getProfileImageUrl())
                .position(workspaceUserResponseDto.getPosition())
                .email(workspaceUserResponseDto.getEmail())
                .phone(workspaceUserResponseDto.getPhone())
                .build();
    }
}
