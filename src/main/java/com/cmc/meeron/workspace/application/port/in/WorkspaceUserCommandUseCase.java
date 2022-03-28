package com.cmc.meeron.workspace.application.port.in;

import com.cmc.meeron.workspace.application.port.in.request.CreateWorkspaceUserRequestDto;
import com.cmc.meeron.workspace.application.port.in.request.KickOutTeamUserRequestDto;
import com.cmc.meeron.workspace.application.port.in.request.JoinTeamUsersRequestDto;
import com.cmc.meeron.workspace.application.port.in.response.WorkspaceUserCommandResponseDto;

public interface WorkspaceUserCommandUseCase {

    WorkspaceUserCommandResponseDto createWorkspaceUser(CreateWorkspaceUserRequestDto createWorkspaceUserRequestDto);

    void joinTeamUsers(JoinTeamUsersRequestDto joinTeamUsersRequestDto);

    void kickOutTeamUser(KickOutTeamUserRequestDto kickOutTeamUserRequestDto);
}
