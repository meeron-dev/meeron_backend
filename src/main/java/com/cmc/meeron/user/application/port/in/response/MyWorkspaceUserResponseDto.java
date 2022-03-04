package com.cmc.meeron.user.application.port.in.response;

import com.cmc.meeron.user.domain.WorkspaceUser;
import lombok.*;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MyWorkspaceUserResponseDto {

    private Long workspaceUserId;
    private Long workspaceId;
    private boolean isWorkspaceAdmin;
    private String nickname;
    private String profileImageUrl;
    private String position;

    public static List<MyWorkspaceUserResponseDto> ofList(List<WorkspaceUser> myWorkspaceUsers) {
        return myWorkspaceUsers.stream()
                .map(MyWorkspaceUserResponseDto::of)
                .collect(Collectors.toList());
    }

    public static MyWorkspaceUserResponseDto of(WorkspaceUser workspaceUser) {
        return MyWorkspaceUserResponseDto.builder()
                .workspaceUserId(workspaceUser.getId())
                .workspaceId(workspaceUser.getWorkspace().getId())
                .isWorkspaceAdmin(workspaceUser.isWorkspaceAdmin())
                .nickname(workspaceUser.getNickname())
                .profileImageUrl(workspaceUser.getProfileImageUrl())
                .position(workspaceUser.getPosition())
                .build();
    }
}
