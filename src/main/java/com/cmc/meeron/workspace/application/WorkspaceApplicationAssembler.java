package com.cmc.meeron.workspace.application;

import com.cmc.meeron.workspace.application.dto.response.WorkspaceResponseDto;
import com.cmc.meeron.workspace.domain.Workspace;

import java.util.List;
import java.util.stream.Collectors;

class WorkspaceApplicationAssembler {

    static List<WorkspaceResponseDto> fromWorkspaces(List<Workspace> workspaces) {
        return workspaces.stream()
                .map(WorkspaceApplicationAssembler::fromWorkspace)
                .collect(Collectors.toList());
    }

    static WorkspaceResponseDto fromWorkspace(Workspace workspace) {
        return WorkspaceResponseDto.builder()
                .workspaceId(workspace.getId())
                .workspaceName(workspace.getName())
                .workspaceLogoUrl(workspace.getWorkspaceImageUrl())
                .build();
    }
}
