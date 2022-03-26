package com.cmc.meeron.workspace.adapter.in.response;

import com.cmc.meeron.workspace.application.port.in.response.WorkspaceUserResponseDto;
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

    public static CreateWorkspaceUserResponse fromDto(WorkspaceUserResponseDto workspaceUserResponseDto) {
        return CreateWorkspaceUserResponse.builder()
                .workspaceUserId(workspaceUserResponseDto.getWorkspaceUserId())
                .isWorkspaceAdmin(workspaceUserResponseDto.isWorkspaceAdmin())
                .nickname(workspaceUserResponseDto.getNickname())
                .position(workspaceUserResponseDto.getPosition())
                .profileImageUrl(workspaceUserResponseDto.getProfileImageUrl())
                .email(workspaceUserResponseDto.getContactMail())
                .phone(workspaceUserResponseDto.getPhone())
                .build();
    }
}
