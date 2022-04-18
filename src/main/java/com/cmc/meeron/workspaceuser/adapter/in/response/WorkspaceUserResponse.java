package com.cmc.meeron.workspaceuser.adapter.in.response;

import com.cmc.meeron.workspaceuser.application.port.in.response.WorkspaceUserQueryResponseDto;
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

    public static WorkspaceUserResponse fromResponseDto(WorkspaceUserQueryResponseDto workspaceUserQueryResponseDto) {
        return WorkspaceUserResponse.builder()
                .workspaceUserId(workspaceUserQueryResponseDto.getWorkspaceUserId())
                .workspaceId(workspaceUserQueryResponseDto.getWorkspaceId())
                .isWorkspaceAdmin(workspaceUserQueryResponseDto.isWorkspaceAdmin())
                .nickname(workspaceUserQueryResponseDto.getNickname())
                .profileImageUrl(workspaceUserQueryResponseDto.getProfileImageUrl())
                .position(workspaceUserQueryResponseDto.getPosition())
                .email(workspaceUserQueryResponseDto.getEmail())
                .phone(workspaceUserQueryResponseDto.getPhone())
                .build();
    }
}
