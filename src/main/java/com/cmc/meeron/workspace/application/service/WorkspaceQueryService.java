package com.cmc.meeron.workspace.application.service;

import com.cmc.meeron.common.exception.workspace.WorkspaceNotFoundException;
import com.cmc.meeron.workspace.application.port.in.response.WorkspaceResponseDto;
import com.cmc.meeron.workspace.application.port.in.WorkspaceQueryUseCase;
import com.cmc.meeron.workspace.domain.Workspace;
import com.cmc.meeron.workspace.application.port.out.WorkspaceQueryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
class WorkspaceQueryService implements WorkspaceQueryUseCase {

    private final WorkspaceQueryPort workspaceQueryPort;

    @Override
    public List<WorkspaceResponseDto> getMyWorkspaces(Long userId) {
        List<Workspace> myWorkspaces = workspaceQueryPort.findMyWorkspaces(userId);
        return WorkspaceResponseDto.ofList(myWorkspaces);
    }

    @Override
    public WorkspaceResponseDto getWorkspace(Long workspaceId) {
        Workspace workspace = workspaceQueryPort.findById(workspaceId)
                .orElseThrow(WorkspaceNotFoundException::new);
        return WorkspaceResponseDto.of(workspace);
    }
}
