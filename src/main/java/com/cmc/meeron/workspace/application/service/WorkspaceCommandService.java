package com.cmc.meeron.workspace.application.service;

import com.cmc.meeron.workspace.application.port.in.WorkspaceCommandUseCase;
import com.cmc.meeron.workspace.application.port.in.response.WorkspaceResponseDto;
import com.cmc.meeron.workspace.application.port.out.WorkspaceCommandPort;
import com.cmc.meeron.workspace.domain.Workspace;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
class WorkspaceCommandService implements WorkspaceCommandUseCase {

    private final WorkspaceCommandPort workspaceCommandPort;

    @Override
    public WorkspaceResponseDto createWorkspace(String name) {
        Workspace workspace = workspaceCommandPort.save(Workspace.of(name));
        return WorkspaceResponseDto.of(workspace);
    }
}
