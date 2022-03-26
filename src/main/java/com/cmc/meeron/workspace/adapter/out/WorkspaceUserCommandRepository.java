package com.cmc.meeron.workspace.adapter.out;

import com.cmc.meeron.workspace.application.port.out.WorkspaceUserCommandPort;
import com.cmc.meeron.workspace.domain.WorkspaceUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class WorkspaceUserCommandRepository implements WorkspaceUserCommandPort {

    private final WorkspaceUserJpaRepository workspaceUserJpaRepository;

    @Override
    public WorkspaceUser saveWorkspaceUser(WorkspaceUser workspaceUser) {
        return workspaceUserJpaRepository.save(workspaceUser);
    }
}
