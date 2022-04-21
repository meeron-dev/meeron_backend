package com.cmc.meeron.team.application.port.in;

import com.cmc.meeron.team.application.port.in.response.TeamResponseDto;
import com.cmc.meeron.team.application.port.in.response.WorkspaceTeamsResponseDto;

import java.util.List;

public interface TeamQueryUseCase {

    List<WorkspaceTeamsResponseDto> getWorkspaceTeams(Long workspaceId);

    TeamResponseDto getMeetingHostTeam(Long meetingId);
}
