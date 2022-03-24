package com.cmc.meeron.team.application.service;

import com.cmc.meeron.common.exception.workspace.WorkspaceNotFoundException;
import com.cmc.meeron.team.application.port.in.TeamCommandUseCase;
import com.cmc.meeron.team.application.port.in.request.CreateTeamRequestDto;
import com.cmc.meeron.team.application.port.out.TeamCommandPort;
import com.cmc.meeron.team.domain.Team;
import com.cmc.meeron.workspace.application.port.out.WorkspaceQueryPort;
import com.cmc.meeron.workspace.domain.Workspace;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
class TeamCommandService implements TeamCommandUseCase {

    private final WorkspaceQueryPort workspaceQueryPort;
    private final TeamCommandPort teamCommandPort;

    @Override
    public Long createTeam(CreateTeamRequestDto createTeamRequestDto) {
        Workspace workspace = workspaceQueryPort.findById(createTeamRequestDto.getWorkspaceId())
                .orElseThrow(WorkspaceNotFoundException::new);
        Team team = Team.of(workspace, createTeamRequestDto.getTeamName());
        return teamCommandPort.save(team);
    }
}