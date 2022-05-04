package com.cmc.meeron.workspaceuser.application.port.in.response;

public class WorkspaceUserResponseDtoBuilder {

    public static WorkspaceUserResponseDto build() {
        return WorkspaceUserResponseDto.builder()
                .workspaceUserId(1L)
                .workspaceId(1L)
                .isWorkspaceAdmin(false)
                .nickname("테스트1")
                .profileImageUrl("https://test.com/123")
                .position("백엔드")
                .email("kobumssh@email.com")
                .phone("010-1234-1234")
                .build();
    }
}
