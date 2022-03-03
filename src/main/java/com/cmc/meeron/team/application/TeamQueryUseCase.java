package com.cmc.meeron.team.application;

import com.cmc.meeron.team.application.dto.response.WorkspaceTeamsResponseDto;

import java.util.List;

public interface TeamQueryUseCase {

    List<WorkspaceTeamsResponseDto> getWorkspaceTeams(Long workspaceId);
}
