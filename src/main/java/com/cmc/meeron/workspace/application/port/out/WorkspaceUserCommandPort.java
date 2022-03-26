package com.cmc.meeron.workspace.application.port.out;

import com.cmc.meeron.workspace.domain.WorkspaceUser;

public interface WorkspaceUserCommandPort {

    WorkspaceUser saveWorkspaceUser(WorkspaceUser workspaceUser);
}
