package com.cmc.meeron.workspace;

import com.cmc.meeron.workspace.domain.Workspace;

public class WorkspaceFixture {

    public static final Workspace WORKSPACE_1 = Workspace.builder()
            .id(1L)
            .name("테스트워크스페이스1")
            .build();

    public static final Workspace WORKSPACE_2 = Workspace.builder()
            .id(2L)
            .name("테스트워크스페이스2")
            .build();
}
