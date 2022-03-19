package com.cmc.meeron.workspace.application.port.out;

import com.cmc.meeron.workspace.domain.Workspace;

public interface WorkspaceCommandPort {

    Workspace save(Workspace workspace);
}
