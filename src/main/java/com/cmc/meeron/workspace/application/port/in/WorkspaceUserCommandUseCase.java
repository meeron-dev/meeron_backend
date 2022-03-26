package com.cmc.meeron.workspace.application.port.in;

import com.cmc.meeron.workspace.application.port.in.request.CreateWorkspaceUserRequestDto;
import com.cmc.meeron.workspace.application.port.in.response.WorkspaceUserResponseDto;

public interface WorkspaceUserCommandUseCase {

    WorkspaceUserResponseDto createWorkspaceUser(CreateWorkspaceUserRequestDto createWorkspaceUserRequestDto);
}
