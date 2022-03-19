package com.cmc.meeron.workspace.adapter.out;

import com.cmc.meeron.workspace.application.port.out.WorkspaceCommandPort;
import com.cmc.meeron.workspace.domain.Workspace;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
class WorkspaceCommandRepository implements WorkspaceCommandPort {

    private final WorkspaceJpaRepository workspaceJpaRepository;

    @Override
    public Workspace save(Workspace workspace) {
        return workspaceJpaRepository.save(workspace);
    }
}
