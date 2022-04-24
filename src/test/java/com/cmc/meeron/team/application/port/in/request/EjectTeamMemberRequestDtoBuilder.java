package com.cmc.meeron.team.application.port.in.request;

public class EjectTeamMemberRequestDtoBuilder {

    public static EjectTeamMemberRequestDto build() {
        return EjectTeamMemberRequestDto.builder()
                .adminWorkspaceUserId(1L)
                .ejectWorkspaceUserId(2L)
                .build();
    }
}
