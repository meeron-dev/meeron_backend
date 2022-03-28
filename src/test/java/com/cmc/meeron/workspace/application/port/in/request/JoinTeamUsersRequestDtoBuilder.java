package com.cmc.meeron.workspace.application.port.in.request;

import java.util.List;

public class JoinTeamUsersRequestDtoBuilder {

    public static JoinTeamUsersRequestDto build() {
        return JoinTeamUsersRequestDto.builder()
                .teamId(1L)
                .workspaceUserIds(List.of(1L, 2L))
                .build();
    }
}
