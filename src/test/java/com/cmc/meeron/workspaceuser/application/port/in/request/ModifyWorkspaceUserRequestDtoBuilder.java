package com.cmc.meeron.workspaceuser.application.port.in.request;

import static com.cmc.meeron.file.FileFixture.FILE;

public class ModifyWorkspaceUserRequestDtoBuilder {

    public static ModifyWorkspaceUserRequestDto build() {
        return ModifyWorkspaceUserRequestDto.builder()
                .workspaceUserId(1L)
                .email("test23@test.com")
                .nickname("테스트닉네임")
                .phone("010-1234-1234")
                .position("수정")
                .profileImage(FILE)
                .build();
    }

    public static ModifyWorkspaceUserRequestDto buildNotExistFile() {
        return ModifyWorkspaceUserRequestDto.builder()
                .workspaceUserId(1L)
                .email("test23@test.com")
                .nickname("테스트닉네임")
                .phone("010-1234-1234")
                .position("수정")
                .build();
    }
}
