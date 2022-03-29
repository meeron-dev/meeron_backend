package com.cmc.meeron.workspace.application.port.in.response;

import com.cmc.meeron.workspace.domain.WorkspaceUser;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WorkspaceUserCommandResponseDto {

    private Long workspaceUserId;
    private String nickname;
    private boolean workspaceAdmin;
    private String position;
    private String profileImageUrl;
    private String contactMail;
    private String phone;

    public static WorkspaceUserCommandResponseDto of(WorkspaceUser workspaceUser) {
        return WorkspaceUserCommandResponseDto.builder()
                .workspaceUserId(workspaceUser.getId())
                .nickname(workspaceUser.getWorkspaceUserInfo().getNickname())
                .workspaceAdmin(workspaceUser.getWorkspaceUserInfo().isWorkspaceAdmin())
                .position(workspaceUser.getWorkspaceUserInfo().getPosition())
                .profileImageUrl(workspaceUser.getWorkspaceUserInfo().getProfileImageUrl())
                .contactMail(workspaceUser.getWorkspaceUserInfo().getContactMail())
                .phone(workspaceUser.getWorkspaceUserInfo().getPhone())
                .build();
    }
}