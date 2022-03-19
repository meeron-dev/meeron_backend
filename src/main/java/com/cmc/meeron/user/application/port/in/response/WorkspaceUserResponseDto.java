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
                .nickname(workspaceUser.getNickname())
                .workspaceAdmin(workspaceUser.isWorkspaceAdmin())
                .position(workspaceUser.getPosition())
                .profileImageUrl(workspaceUser.getProfileImageUrl())
                .contactMail(workspaceUser.getContactMail())
                .build();
    }
}
