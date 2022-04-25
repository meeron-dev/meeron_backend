package com.cmc.meeron.team.application.service;

import com.cmc.meeron.common.advice.workspaceuser.CheckWorkspaceAdmin;
import com.cmc.meeron.common.exception.team.TeamNotFoundException;
import com.cmc.meeron.common.exception.workspace.NotAllFoundWorkspaceUsersException;
import com.cmc.meeron.common.exception.workspace.WorkspaceUserNotFoundException;
import com.cmc.meeron.team.application.port.in.TeamMemberManageUseCase;
import com.cmc.meeron.team.application.port.in.request.EjectTeamMemberRequestDto;
import com.cmc.meeron.team.application.port.in.request.JoinTeamMembersRequestDto;
import com.cmc.meeron.team.application.port.out.TeamQueryPort;
import com.cmc.meeron.team.application.port.out.TeamToWorkspaceUserQueryPort;
import com.cmc.meeron.team.domain.Team;
import com.cmc.meeron.workspaceuser.domain.WorkspaceUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
class TeamMemberManageService implements TeamMemberManageUseCase {

    private final TeamQueryPort teamQueryPort;
    private final TeamToWorkspaceUserQueryPort teamToWorkspaceUserQueryPort;

    @CheckWorkspaceAdmin
    @Override
    public void joinTeamMembers(JoinTeamMembersRequestDto joinTeamMembersRequestDto) {
        Team team = teamQueryPort.findById(joinTeamMembersRequestDto.getTeamId())
                .orElseThrow(TeamNotFoundException::new);
        List<Long> joinTeamWorkspaceUserIds = joinTeamMembersRequestDto.getWorkspaceUserIds();
        List<WorkspaceUser> workspaceUsers = teamToWorkspaceUserQueryPort
                .findAllWorkspaceUsersByIds(joinTeamWorkspaceUserIds);
        if (workspaceUsers.size() != joinTeamWorkspaceUserIds.size()) {
            throw new NotAllFoundWorkspaceUsersException();
        }
        workspaceUsers.forEach(workspaceUser -> workspaceUser.joinTeam(team));
    }

    @CheckWorkspaceAdmin
    @Override
    public void ejectTeamMember(EjectTeamMemberRequestDto ejectTeamMemberRequestDto) {
        teamToWorkspaceUserQueryPort
                .findById(ejectTeamMemberRequestDto.getEjectWorkspaceUserId())
                .orElseThrow(WorkspaceUserNotFoundException::new)
                .exitTeam();
    }
}
