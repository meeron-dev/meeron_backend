package com.cmc.meeron.workspace.presentation;

import com.cmc.meeron.workspace.application.dto.response.WorkspaceResponseDto;
import com.cmc.meeron.workspace.presentation.dto.response.MyWorkspacesResponse;
import com.cmc.meeron.workspace.presentation.dto.response.WorkspaceResponse;

import java.util.List;
import java.util.stream.Collectors;

class WorkspacePresentationAssembler {

    static MyWorkspacesResponse fromWorkspaceResponseDtos(List<WorkspaceResponseDto> myWorkspaces) {
        return MyWorkspacesResponse.builder()
                .myWorkspaces(myWorkspaces.stream()
                        .map(WorkspacePresentationAssembler::fromWorkspaceResponseDto)
                        .collect(Collectors.toList()))
                .build();
    }

    static WorkspaceResponse fromWorkspaceResponseDto(WorkspaceResponseDto response) {
        return WorkspaceResponse.builder()
                .workspaceId(response.getWorkspaceId())
                .workspaceName(response.getWorkspaceName())
                .workspaceLogoUrl(response.getWorkspaceLogoUrl())
                .build();
    }
}
