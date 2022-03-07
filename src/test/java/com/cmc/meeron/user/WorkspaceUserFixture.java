package com.cmc.meeron.user;

import com.cmc.meeron.user.domain.WorkspaceUser;

import static com.cmc.meeron.workspace.WorkspaceFixture.WORKSPACE_1;
import static com.cmc.meeron.workspace.WorkspaceFixture.WORKSPACE_2;

public class WorkspaceUserFixture {

    public static final WorkspaceUser WORKSPACE_USER_1 = WorkspaceUser.builder()
            .id(59L)
            .workspace(WORKSPACE_1)
            .nickname("무무")
            .build();

    public static final WorkspaceUser WORKSPACE_USER_2 = WorkspaceUser.builder()
            .id(14L)
            .workspace(WORKSPACE_2)
            .nickname("누구세요")
            .build();
}
