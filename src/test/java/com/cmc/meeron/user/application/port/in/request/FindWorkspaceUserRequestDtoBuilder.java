package com.cmc.meeron.user.application.port.in.request;

import com.cmc.meeron.workspaceuser.application.port.in.request.FindWorkspaceUserRequestDto;

public class FindWorkspaceUserRequestDtoBuilder {

    public static FindWorkspaceUserRequestDto build() {
        return FindWorkspaceUserRequestDto.builder()
                .nickname("test")
                .workspaceId(1L)
                .build();
    }
}
