package com.cmc.meeron.workspaceuser.application.port.in.request;

import com.cmc.meeron.workspaceuser.application.port.in.request.KickOutTeamUserRequestDto;

public class KickOutTeamUserRequestDtoBuilder {

    public static KickOutTeamUserRequestDto build() {
        return KickOutTeamUserRequestDto.builder()
                .adminWorkspaceUserId(1L)
                .kickOutWorkspaceUserId(2L)
                .build();
    }
}
