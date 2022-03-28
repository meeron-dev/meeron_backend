package com.cmc.meeron.workspace.adapter.in.response;

import com.cmc.meeron.workspace.application.port.in.response.WorkspaceUserCommandResponseDto;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateWorkspaceUserResponse {

    private Long workspaceUserId;
    private boolean isWorkspaceAdmin;
    private String nickname;
    private String position;
    private String profileImageUrl;
    private String email;
    private String phone;

    public static CreateWorkspaceUserResponse fromDto(WorkspaceUserCommandResponseDto workspaceUserCommandResponseDto) {
        return CreateWorkspaceUserResponse.builder()
                .workspaceUserId(workspaceUserCommandResponseDto.getWorkspaceUserId())
                .isWorkspaceAdmin(workspaceUserCommandResponseDto.isWorkspaceAdmin())
                .nickname(workspaceUserCommandResponseDto.getNickname())
                .position(workspaceUserCommandResponseDto.getPosition())
                .profileImageUrl(workspaceUserCommandResponseDto.getProfileImageUrl())
                .email(workspaceUserCommandResponseDto.getContactMail())
                .phone(workspaceUserCommandResponseDto.getPhone())
                .build();
    }
}
