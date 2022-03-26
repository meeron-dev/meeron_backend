package com.cmc.meeron.workspace.application.port.in;

import com.cmc.meeron.workspace.application.port.in.request.FindWorkspaceUserRequestDto;
import com.cmc.meeron.workspace.application.port.in.response.MyWorkspaceUserResponseDto;

import java.util.List;

public interface WorkspaceUserQueryUseCase {

    List<MyWorkspaceUserResponseDto> getMyWorkspaceUsers(Long userId);

    MyWorkspaceUserResponseDto getMyWorkspaceUser(Long workspaceUserId);

    List<MyWorkspaceUserResponseDto> searchWorkspaceUsers(FindWorkspaceUserRequestDto findWorkspaceUserRequestDto);

    List<MyWorkspaceUserResponseDto> getTeamUsers(Long teamId);

    void checkDuplicateNickname(FindWorkspaceUserRequestDto findWorkspaceUserRequestDto);
}
