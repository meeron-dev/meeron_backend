package com.cmc.meeron.user.application;

import com.cmc.meeron.user.application.dto.response.MyWorkspaceUserResponseDto;
import com.cmc.meeron.user.domain.WorkspaceUser;

import java.util.List;
import java.util.stream.Collectors;

class UserApplicationAssembler {

    static List<MyWorkspaceUserResponseDto> fromWorkspaceUsers(List<WorkspaceUser> myWorkspaceUsers) {
        return myWorkspaceUsers.stream()
                .map(UserApplicationAssembler::fromWorkspaceUser)
                .collect(Collectors.toList());
    }

    public static MyWorkspaceUserResponseDto fromWorkspaceUser(WorkspaceUser workspaceUser) {
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
