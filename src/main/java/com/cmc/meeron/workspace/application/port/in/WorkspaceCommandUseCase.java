package com.cmc.meeron.workspace.application.port.in;

import com.cmc.meeron.workspace.application.port.in.response.WorkspaceResponseDto;

public interface WorkspaceCommandUseCase {

    WorkspaceResponseDto createWorkspace(String name);
}
