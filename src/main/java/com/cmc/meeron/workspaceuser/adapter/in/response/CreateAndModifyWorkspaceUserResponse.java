package com.cmc.meeron.workspaceuser.adapter.in.response;

import com.cmc.meeron.workspaceuser.application.port.in.response.WorkspaceUserCommandResponseDto;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateAndModifyWorkspaceUserResponse {

    private Long workspaceUserId;
    private boolean isWorkspaceAdmin;
    private String nickname;
    private String position;
    private String profileImageUrl;
    private String email;
    private String phone;

    public static CreateAndModifyWorkspaceUserResponse fromResponseDto(WorkspaceUserCommandResponseDto workspaceUserCommandResponseDto) {
        return CreateAndModifyWorkspaceUserResponse.builder()
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
