package com.cmc.meeron.workspaceuser.application.port.in;

import com.cmc.meeron.workspaceuser.application.port.in.request.FindWorkspaceUserRequestDto;
import com.cmc.meeron.workspaceuser.application.port.in.response.UserResponseDto;
import com.cmc.meeron.workspaceuser.application.port.in.response.WorkspaceUserQueryResponseDto;

import java.util.List;

public interface WorkspaceUserQueryUseCase {

    List<WorkspaceUserQueryResponseDto> getMyWorkspaceUsers(Long userId);

    WorkspaceUserQueryResponseDto getMyWorkspaceUser(Long workspaceUserId);

    List<WorkspaceUserQueryResponseDto> searchWorkspaceUsers(FindWorkspaceUserRequestDto findWorkspaceUserRequestDto);

    List<WorkspaceUserQueryResponseDto> getTeamUsers(Long teamId);

    void checkDuplicateNickname(FindWorkspaceUserRequestDto findWorkspaceUserRequestDto);

    List<WorkspaceUserQueryResponseDto> getNoneTeamWorkspaceUsers(Long workspaceId);

    UserResponseDto getUser(Long workspaceUserId);
}
