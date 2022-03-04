package com.cmc.meeron.workspace.application.port.in;

import com.cmc.meeron.workspace.application.port.in.response.WorkspaceResponseDto;

import java.util.List;

public interface WorkspaceQueryUseCase {

    List<WorkspaceResponseDto> getMyWorkspaces(Long userId);

    WorkspaceResponseDto getWorkspace(Long workspaceId);
}
