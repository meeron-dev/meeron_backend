package com.cmc.meeron.team.application.port.in.request;

import java.util.List;

public class JoinTeamMembersRequestDtoBuilder {

    public static JoinTeamMembersRequestDto build() {
        return JoinTeamMembersRequestDto.builder()
                .teamId(1L)
                .workspaceUserIds(List.of(1L, 2L))
                .build();
    }
}
