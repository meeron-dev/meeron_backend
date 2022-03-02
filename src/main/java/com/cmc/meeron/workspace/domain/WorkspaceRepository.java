package com.cmc.meeron.workspace.domain;

import java.util.List;
import java.util.Optional;

public interface WorkspaceRepository {

    List<Workspace> findMyWorkspaces(Long userId);

    Optional<Workspace> findById(Long workspaceId);
}
