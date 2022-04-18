package com.cmc.meeron.workspaceuser.application.port.in.response;

public class WorkspaceUserCommandResponseDtoBuilder {

    public static WorkspaceUserCommandResponseDto build() {
        return WorkspaceUserCommandResponseDto.builder()
                .workspaceUserId(1L)
                .contactMail("change@change.com")
                .nickname("변경닉네임")
                .phone("010-1234-1234")
                .position("변경됨")
                .profileImageUrl("https://change.change.com")
                .workspaceAdmin(false)
                .build();
    }
}
