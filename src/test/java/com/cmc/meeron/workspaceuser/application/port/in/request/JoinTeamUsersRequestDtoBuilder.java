package com.cmc.meeron.workspaceuser.application.port.in.request;

import com.cmc.meeron.workspaceuser.application.port.in.request.JoinTeamUsersRequestDto;

import java.util.List;

public class JoinTeamUsersRequestDtoBuilder {

    public static JoinTeamUsersRequestDto build() {
        return JoinTeamUsersRequestDto.builder()
                .teamId(1L)
                .workspaceUserIds(List.of(1L, 2L))
                .build();
    }
}
