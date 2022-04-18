package com.cmc.meeron.workspaceuser.application.port.out;

import com.cmc.meeron.workspaceuser.domain.WorkspaceUser;

public interface WorkspaceUserCommandPort {

    WorkspaceUser saveWorkspaceUser(WorkspaceUser workspaceUser);
}
