package com.cmc.meeron.team.application.port.in.request;

public class DeleteTeamRequestDtoBuilder {

    public static DeleteTeamRequestDto build() {
        return DeleteTeamRequestDto.builder()
                .teamId(1L)
                .adminWorkspaceUserId(2L)
                .build();
    }
}
