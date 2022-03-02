package com.cmc.meeron.workspace.application;

import com.cmc.meeron.common.exception.workspace.WorkspaceNotFoundException;
import com.cmc.meeron.workspace.application.dto.response.WorkspaceResponseDto;
import com.cmc.meeron.workspace.domain.Workspace;
import com.cmc.meeron.workspace.domain.WorkspaceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
class WorkspaceService implements WorkspaceUseCase {

    private final WorkspaceRepository workspaceRepository;

    @Override
    public List<WorkspaceResponseDto> getMyWorkspaces(Long userId) {
        List<Workspace> myWorkspaces = workspaceRepository.findMyWorkspaces(userId);
        return WorkspaceApplicationAssembler.fromWorkspaces(myWorkspaces);
    }

    @Override
    public WorkspaceResponseDto getWorkspace(Long workspaceId) {
        Workspace workspace = workspaceRepository.findById(workspaceId)
                .orElseThrow(WorkspaceNotFoundException::new);
        return WorkspaceApplicationAssembler.fromWorkspace(workspace);
    }
}
