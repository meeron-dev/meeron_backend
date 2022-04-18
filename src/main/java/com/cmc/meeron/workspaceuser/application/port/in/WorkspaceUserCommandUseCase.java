package com.cmc.meeron.workspaceuser.application.port.in;

import com.cmc.meeron.workspaceuser.application.port.in.request.CreateWorkspaceUserRequestDto;
import com.cmc.meeron.workspaceuser.application.port.in.request.JoinTeamUsersRequestDto;
import com.cmc.meeron.workspaceuser.application.port.in.request.KickOutTeamUserRequestDto;
import com.cmc.meeron.workspaceuser.application.port.in.request.ModifyWorkspaceUserRequestDto;
import com.cmc.meeron.workspaceuser.application.port.in.response.WorkspaceUserCommandResponseDto;

public interface WorkspaceUserCommandUseCase {

    WorkspaceUserCommandResponseDto createWorkspaceUser(CreateWorkspaceUserRequestDto createWorkspaceUserRequestDto);

    WorkspaceUserCommandResponseDto modifyWorkspaceUser(ModifyWorkspaceUserRequestDto modifyWorkspaceUserRequestDto);

    void joinTeamUsers(JoinTeamUsersRequestDto joinTeamUsersRequestDto);

    void kickOutTeamUser(KickOutTeamUserRequestDto kickOutTeamUserRequestDto);
}
