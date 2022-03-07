package com.cmc.meeron.workspace.adapter.out;

import com.cmc.meeron.workspace.domain.Workspace;
import com.cmc.meeron.workspace.application.port.out.WorkspaceQueryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
class WorkspaceQueryRepository implements WorkspaceQueryPort {

    private final WorkspaceJpaRepository workspaceJpaRepository;

    @Override
    public List<Workspace> findMyWorkspaces(Long userId) {
        return workspaceJpaRepository.findByUserId(userId);
    }

    @Override
    public Optional<Workspace> findById(Long workspaceId) {
        return workspaceJpaRepository.findById(workspaceId);
    }

    @Override
    public List<Workspace> findByWorkspaceUserIds(List<Long> workspaceUserIds) {
        return workspaceJpaRepository.findByWorkspaceUserIds(workspaceUserIds);
    }
}
