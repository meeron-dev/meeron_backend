package com.cmc.meeron.team.application.port.in;

import com.cmc.meeron.team.application.port.in.response.TeamResponseDto;

import java.util.List;

public interface TeamQueryUseCase {

    List<TeamResponseDto> getWorkspaceTeams(Long workspaceId);

    TeamResponseDto getMeetingHostTeam(Long meetingId);
}
