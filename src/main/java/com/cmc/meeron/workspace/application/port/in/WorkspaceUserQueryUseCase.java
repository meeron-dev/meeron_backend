package com.cmc.meeron.workspace.application.port.in;

import com.cmc.meeron.workspace.application.port.in.request.FindWorkspaceUserRequestDto;
import com.cmc.meeron.workspace.application.port.in.response.WorkspaceUserQueryResponseDto;

import java.util.List;

public interface WorkspaceUserQueryUseCase {

    List<WorkspaceUserQueryResponseDto> getMyWorkspaceUsers(Long userId);

    WorkspaceUserQueryResponseDto getMyWorkspaceUser(Long workspaceUserId);

    List<WorkspaceUserQueryResponseDto> searchWorkspaceUsers(FindWorkspaceUserRequestDto findWorkspaceUserRequestDto);

    List<WorkspaceUserQueryResponseDto> getTeamUsers(Long teamId);

    void checkDuplicateNickname(FindWorkspaceUserRequestDto findWorkspaceUserRequestDto);

    List<WorkspaceUserQueryResponseDto> getNoneTeamWorkspaceUsers(Long workspaceId);
}