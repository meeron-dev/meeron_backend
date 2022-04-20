package com.cmc.meeron.attendee.application.port.in.response;

import com.cmc.meeron.workspaceuser.domain.WorkspaceUser;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AttendeeWorkspaceUserResponseDto {

    private Long workspaceUserId;
    private Long workspaceId;
    private boolean isWorkspaceAdmin;
    private String nickname;
    private String position;
    private String profileImageUrl;
    private String email;
    private String phone;

    public static AttendeeWorkspaceUserResponseDto fromEntity(WorkspaceUser workspaceUser) {
        return AttendeeWorkspaceUserResponseDto.builder()
                .workspaceUserId(workspaceUser.getId())
                .workspaceId(workspaceUser.getWorkspace().getId())
                .isWorkspaceAdmin(workspaceUser.isAdmin())
                .nickname(workspaceUser.getWorkspaceUserInfo().getNickname())
                .position(workspaceUser.getWorkspaceUserInfo().getPosition())
                .profileImageUrl(workspaceUser.getWorkspaceUserInfo().getProfileImageUrl())
                .email(workspaceUser.getWorkspaceUserInfo().getContactMail())
                .phone(workspaceUser.getWorkspaceUserInfo().getPhone())
                .build();
    }
}
