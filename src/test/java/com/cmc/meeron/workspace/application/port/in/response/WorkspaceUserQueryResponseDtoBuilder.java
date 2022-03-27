package com.cmc.meeron.workspace.application.port.in.response;

import java.util.List;

public class WorkspaceUserQueryResponseDtoBuilder {

    public static List<WorkspaceUserQueryResponseDto> build() {
        return List.of(
                WorkspaceUserQueryResponseDto.builder()
                        .workspaceUserId(1L)
                        .workspaceId(1L)
                        .isWorkspaceAdmin(false)
                        .nickname("테스트1")
                        .profileImageUrl("")
                        .position("")
                        .email("")
                        .build(),
                WorkspaceUserQueryResponseDto.builder()
                        .workspaceUserId(2L)
                        .workspaceId(1L)
                        .isWorkspaceAdmin(false)
                        .nickname("테스트2")
                        .profileImageUrl("")
                        .position("개발자")
                        .email("test2@test.com")
                        .build()
        );
    }
}
