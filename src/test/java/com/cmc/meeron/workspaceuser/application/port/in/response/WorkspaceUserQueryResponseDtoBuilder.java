package com.cmc.meeron.workspaceuser.application.port.in.response;

import java.util.List;

public class WorkspaceUserQueryResponseDtoBuilder {

    public static List<WorkspaceUserResponseDto> buildList() {
        return List.of(
                WorkspaceUserResponseDto.builder()
                        .workspaceUserId(1L)
                        .workspaceId(1L)
                        .isWorkspaceAdmin(false)
                        .nickname("테스트1")
                        .profileImageUrl("")
                        .position("")
                        .email("")
                        .phone("010-1234-1234")
                        .build(),
                WorkspaceUserResponseDto.builder()
                        .workspaceUserId(2L)
                        .workspaceId(1L)
                        .isWorkspaceAdmin(false)
                        .nickname("테스트2")
                        .profileImageUrl("")
                        .position("개발자")
                        .email("test2@test.com")
                        .phone("010-1234-1235")
                        .build()
        );
    }
}
