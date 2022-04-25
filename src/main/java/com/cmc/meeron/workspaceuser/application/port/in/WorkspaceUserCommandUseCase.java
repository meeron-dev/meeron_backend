package com.cmc.meeron.workspaceuser.application.port.in;

import com.cmc.meeron.workspaceuser.application.port.in.request.CreateWorkspaceUserRequestDto;
import com.cmc.meeron.team.application.port.in.request.JoinTeamMembersRequestDto;
import com.cmc.meeron.team.application.port.in.request.EjectTeamMemberRequestDto;
import com.cmc.meeron.workspaceuser.application.port.in.request.ModifyWorkspaceUserRequestDto;
import com.cmc.meeron.workspaceuser.application.port.in.response.WorkspaceUserResponseDto;

public interface WorkspaceUserCommandUseCase {

    WorkspaceUserResponseDto createWorkspaceUser(CreateWorkspaceUserRequestDto createWorkspaceUserRequestDto);

    WorkspaceUserResponseDto modifyWorkspaceUser(ModifyWorkspaceUserRequestDto modifyWorkspaceUserRequestDto);

    @Deprecated
    void joinTeamUsers(JoinTeamMembersRequestDto joinTeamMembersRequestDto);

    @Deprecated
    void kickOutTeamUser(EjectTeamMemberRequestDto ejectTeamMemberRequestDto);
}
