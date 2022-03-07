package com.cmc.meeron.workspace.application.port.out;

import com.cmc.meeron.workspace.domain.Workspace;

import java.util.List;
import java.util.Optional;

public interface WorkspaceQueryPort {

    List<Workspace> findMyWorkspaces(Long userId);

    Optional<Workspace> findById(Long workspaceId);

    List<Workspace> findByWorkspaceUserIds(List<Long> workspaceUserIds);
}
