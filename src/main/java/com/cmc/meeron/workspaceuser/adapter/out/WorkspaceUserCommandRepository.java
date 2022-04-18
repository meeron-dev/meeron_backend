package com.cmc.meeron.workspaceuser.adapter.out;

import com.cmc.meeron.workspaceuser.application.port.out.WorkspaceUserCommandPort;
import com.cmc.meeron.workspaceuser.domain.WorkspaceUser;
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
