package com.cmc.meeron.team.application.service;

import com.cmc.meeron.common.advice.workspaceuser.CheckWorkspaceAdmin;
import com.cmc.meeron.common.exception.team.TeamCountsConditionException;
import com.cmc.meeron.common.exception.team.TeamNotFoundException;
import com.cmc.meeron.common.exception.workspace.WorkspaceNotFoundException;
import com.cmc.meeron.team.application.port.in.TeamCommandUseCase;
import com.cmc.meeron.team.application.port.in.request.CreateTeamRequestDto;
import com.cmc.meeron.team.application.port.in.request.DeleteTeamRequestDto;
import com.cmc.meeron.team.application.port.in.request.ModifyTeamNameRequestDto;
import com.cmc.meeron.team.application.port.out.TeamCommandPort;
import com.cmc.meeron.team.application.port.out.TeamQueryPort;
import com.cmc.meeron.team.application.port.out.TeamToWorkspaceQueryPort;
import com.cmc.meeron.team.application.port.out.TeamToWorkspaceUserQueryPort;
import com.cmc.meeron.team.domain.Team;
import com.cmc.meeron.workspace.domain.Workspace;
import com.cmc.meeron.workspaceuser.domain.WorkspaceUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
class TeamCommandService implements TeamCommandUseCase {

    private final TeamQueryPort teamQueryPort;
    private final TeamCommandPort teamCommandPort;
    private final TeamToWorkspaceQueryPort teamToWorkspaceQueryPort;
    private final TeamToWorkspaceUserQueryPort teamToWorkspaceUserQueryPort;

    // TODO: 2022/03/28 kobeomseok95 팀 생성시 관리자인지 체크
    @Override
    public Long createTeam(CreateTeamRequestDto createTeamRequestDto) {
        Workspace workspace = teamToWorkspaceQueryPort.findById(createTeamRequestDto.getWorkspaceId())
                .orElseThrow(WorkspaceNotFoundException::new);
        validCountTeam(workspace.getId());
        Team team = Team.of(workspace, createTeamRequestDto.getTeamName());
        return teamCommandPort.save(team);
    }

    private void validCountTeam(Long workspaceId) {
        if (teamQueryPort.countByWorkspaceId(workspaceId) >= 5) {
            throw new TeamCountsConditionException();
        }
    }

    @Override
    @CheckWorkspaceAdmin
    public void deleteTeam(DeleteTeamRequestDto deleteTeamRequestDto) {
        Long teamId = deleteTeamRequestDto.getTeamId();
        List<WorkspaceUser> teamUsers = teamToWorkspaceUserQueryPort.findByTeamId(teamId);
        teamUsers.forEach(WorkspaceUser::exitTeam);
        teamQueryPort.findById(teamId)
                .ifPresent(team -> teamCommandPort.deleteById(team.getId()));
    }

    @Override
    @CheckWorkspaceAdmin
    public void modifyTeamName(ModifyTeamNameRequestDto modifyTeamNameRequestDto) {
        Team team = teamQueryPort.findById(modifyTeamNameRequestDto.getTeamId())
                .orElseThrow(TeamNotFoundException::new);
        team.setName(modifyTeamNameRequestDto.getName());
    }
}
