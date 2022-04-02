package com.cmc.meeron.workspace;

import com.cmc.meeron.workspace.domain.WorkspaceUserInfo;

public class WorkspaceUserInfoFixture {

    public static final WorkspaceUserInfo WORKSPACE_USER_INFO = WorkspaceUserInfo.builder()
            .contactMail("after@test.com")
            .nickname("after")
            .phone("010-1234-1234")
            .position("변경")
            .profileImageUrl("https://test.test.com/123")
            .build();
}
