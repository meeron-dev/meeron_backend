package com.cmc.meeron.user.application.port.in.response;

import com.cmc.meeron.user.domain.WorkspaceUser;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WorkspaceUserResponseDto {

    private Long workspaceUserId;
    private String nickname;
    private boolean workspaceAdmin;
    private String position;
    private String profileImageUrl;
    private String contactMail;
    private String phone;

    public static WorkspaceUserResponseDto of(WorkspaceUser workspaceUser) {
        return WorkspaceUserResponseDto.builder()
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
