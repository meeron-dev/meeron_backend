package com.cmc.meeron.workspaceuser.application.port.in;

import com.cmc.meeron.workspaceuser.application.port.in.request.FindWorkspaceUserRequestDto;
import com.cmc.meeron.workspaceuser.application.port.in.response.WorkspaceUserResponseDto;

import java.util.List;

public interface WorkspaceUserQueryUseCase {

    List<WorkspaceUserResponseDto> getMyWorkspaceUsers(Long userId);

    WorkspaceUserResponseDto getMyWorkspaceUser(Long workspaceUserId);

    List<WorkspaceUserResponseDto> searchWorkspaceUsers(FindWorkspaceUserRequestDto findWorkspaceUserRequestDto);

    List<WorkspaceUserResponseDto> getTeamUsers(Long teamId);

    void checkDuplicateNickname(FindWorkspaceUserRequestDto findWorkspaceUserRequestDto);

    List<WorkspaceUserResponseDto> getNoneTeamWorkspaceUsers(Long workspaceId);
}
