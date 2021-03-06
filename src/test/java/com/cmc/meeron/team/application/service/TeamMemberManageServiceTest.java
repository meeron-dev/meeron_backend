package com.cmc.meeron.team.application.service;

import com.cmc.meeron.common.exception.team.TeamNotFoundException;
import com.cmc.meeron.common.exception.workspace.NotAllFoundWorkspaceUsersException;
import com.cmc.meeron.common.exception.workspace.WorkspaceUserNotFoundException;
import com.cmc.meeron.team.application.port.in.request.EjectTeamMemberRequestDto;
import com.cmc.meeron.team.application.port.in.request.JoinTeamMembersRequestDto;
import com.cmc.meeron.team.application.port.in.request.JoinTeamMembersRequestDtoBuilder;
import com.cmc.meeron.team.application.port.in.request.EjectTeamMemberRequestDtoBuilder;
import com.cmc.meeron.team.application.port.out.TeamQueryPort;
import com.cmc.meeron.team.application.port.out.TeamToWorkspaceUserQueryPort;
import com.cmc.meeron.team.domain.Team;
import com.cmc.meeron.workspaceuser.domain.WorkspaceUser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.cmc.meeron.team.TeamFixture.TEAM_1;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TeamMemberManageServiceTest {

    @Mock
    TeamQueryPort teamQueryPort;
    @Mock
    TeamToWorkspaceUserQueryPort teamToWorkspaceUserQueryPort;
    @InjectMocks
    TeamMemberManageService teamMemberManageService;

    WorkspaceUser workspaceUser1;
    WorkspaceUser workspaceUser2;
    WorkspaceUser ejectWorkspaceUser;

    @BeforeEach
    void setUp() {
        workspaceUser1 = WorkspaceUser.builder().id(1L).build();
        workspaceUser2 = WorkspaceUser.builder().id(2L).build();
        ejectWorkspaceUser = WorkspaceUser.builder().id(3L).team(TEAM_1).build();
    }

    @DisplayName("?????? ?????? - ?????? / ?????? ?????? ??????")
    @Test
    void join_team_member_fail_not_found_team() throws Exception {

        // given
        JoinTeamMembersRequestDto requestDto = JoinTeamMembersRequestDtoBuilder.build();
        when(teamQueryPort.findById(any()))
                .thenReturn(Optional.empty());

        // when, then
        assertThrows(TeamNotFoundException.class,
                () -> teamMemberManageService.joinTeamMembers(requestDto));
    }

    @DisplayName("?????? ?????? - ?????? / ????????? ?????????????????? ????????? ?????? ???, DB?????? ????????? ?????? ?????? ?????? ?????? ??????")
    @Test
    void join_team_member_fail_not_equal_find_workspace_users() throws Exception {

        // given
        JoinTeamMembersRequestDto requestDto = JoinTeamMembersRequestDtoBuilder.build();
        when(teamQueryPort.findById(any()))
                .thenReturn(Optional.of(TEAM_1));
        when(teamToWorkspaceUserQueryPort.findAllWorkspaceUsersByIds(any()))
                .thenReturn(List.of(workspaceUser1));

        // when, then
        assertThrows(NotAllFoundWorkspaceUsersException.class,
                () -> teamMemberManageService.joinTeamMembers(requestDto));
    }

    @DisplayName("?????? ?????? - ??????")
    @Test
    void join_team_members_success() throws Exception {

        // given
        JoinTeamMembersRequestDto requestDto = JoinTeamMembersRequestDtoBuilder.build();
        when(teamQueryPort.findById(any()))
                .thenReturn(Optional.of(TEAM_1));
        List<WorkspaceUser> joinTeamMembers = List.of(this.workspaceUser1, workspaceUser2);
        when(teamToWorkspaceUserQueryPort.findAllWorkspaceUsersByIds(any()))
                .thenReturn(joinTeamMembers);

        // when
        teamMemberManageService.joinTeamMembers(requestDto);

        // then
        List<Team> teams = joinTeamMembers.stream().map(WorkspaceUser::getTeam).collect(Collectors.toList());
        assertAll(
                () -> verify(teamQueryPort).findById(requestDto.getTeamId()),
                () -> verify(teamToWorkspaceUserQueryPort).findAllWorkspaceUsersByIds(requestDto.getWorkspaceUserIds()),
                () -> assertThat(teams).containsOnly(TEAM_1)
        );
    }

    @DisplayName("????????? ??????????????? - ?????? / ?????????????????? ????????? ???????????? ?????? ??????")
    @Test
    void eject_team_member_fail_not_found_workspace_user() throws Exception {

        // given
        EjectTeamMemberRequestDto requestDto = EjectTeamMemberRequestDtoBuilder.build();
        when(teamToWorkspaceUserQueryPort.findById(any()))
                .thenReturn(Optional.empty());

        // when, then
        assertThrows(WorkspaceUserNotFoundException.class,
                () -> teamMemberManageService.ejectTeamMember(requestDto));
    }

    @DisplayName("????????? ??????????????? - ??????")
    @Test
    void eject_team_member_success() throws Exception {

        // given
        EjectTeamMemberRequestDto requestDto = EjectTeamMemberRequestDtoBuilder.build();
        when(teamToWorkspaceUserQueryPort.findById(any()))
                .thenReturn(Optional.of(ejectWorkspaceUser));

        // when
        teamMemberManageService.ejectTeamMember(requestDto);

        // then
        assertAll(
                () -> verify(teamToWorkspaceUserQueryPort).findById(requestDto.getEjectWorkspaceUserId()),
                () -> assertNull(ejectWorkspaceUser.getTeam())
        );
    }
}
