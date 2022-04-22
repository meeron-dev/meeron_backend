package com.cmc.meeron.team.application.service;

import com.cmc.meeron.common.exception.team.TeamNotFoundException;
import com.cmc.meeron.team.application.port.in.TeamQueryUseCase;
import com.cmc.meeron.team.application.port.in.response.TeamResponseDto;
import com.cmc.meeron.team.application.port.out.TeamQueryPort;
import com.cmc.meeron.team.domain.Team;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
class TeamQueryService implements TeamQueryUseCase {

    private final TeamQueryPort teamQueryPort;

    @Override
    public List<TeamResponseDto> getWorkspaceTeams(Long workspaceId) {
        List<Team> teams = teamQueryPort.findByWorkspaceId(workspaceId);
        return TeamResponseDto.from(teams);
    }

    @Override
    public TeamResponseDto getMeetingHostTeam(Long meetingId) {
        Team team = teamQueryPort.findByMeetingId(meetingId)
                .orElseThrow(TeamNotFoundException::new);
        return TeamResponseDto.from(team);
    }
}
