package com.cmc.meeron.workspace.application;

import com.cmc.meeron.workspace.application.dto.response.WorkspaceResponseDto;

import java.util.List;

public interface WorkspaceUseCase {

    List<WorkspaceResponseDto> getMyWorkspaces(Long userId);

    WorkspaceResponseDto getWorkspace(Long workspaceId);
}
