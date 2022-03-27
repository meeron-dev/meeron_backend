package com.cmc.meeron.workspace.application.port.in.request;

public class KickOutTeamUserRequestDtoBuilder {

    public static KickOutTeamUserRequestDto build() {
        return KickOutTeamUserRequestDto.builder()
                .adminWorkspaceUserId(1L)
                .kickOutWorkspaceUserId(2L)
                .build();
    }
}
