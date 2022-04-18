package com.cmc.meeron.team.application.port.out;

import com.cmc.meeron.workspace.domain.Workspace;

import java.util.Optional;

public interface TeamToWorkspaceQueryPort {

    Optional<Workspace> findById(Long workspaceId);
}
