package com.cmc.meeron.team.application.service;

import com.cmc.meeron.common.exception.team.TeamCountsConditionException;
import com.cmc.meeron.common.exception.team.TeamNotFoundException;
import com.cmc.meeron.common.exception.workspace.WorkspaceNotFoundException;
import com.cmc.meeron.team.application.port.in.request.*;
import com.cmc.meeron.team.application.port.out.TeamCommandPort;
import com.cmc.meeron.team.application.port.out.TeamQueryPort;
import com.cmc.meeron.team.domain.Team;
import com.cmc.meeron.workspace.application.port.out.WorkspaceQueryPort;
import com.cmc.meeron.workspace.application.port.out.WorkspaceUserQueryPort;
import com.cmc.meeron.workspace.domain.Workspace;
import com.cmc.meeron.workspace.domain.WorkspaceUser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static com.cmc.meeron.team.TeamFixture.TEAM_1;
import static com.cmc.meeron.workspace.WorkspaceFixture.WORKSPACE_1;
import static com.cmc.meeron.workspace.WorkspaceUserFixture.WORKSPACE_USER_ADMIN;
import static com.cmc.meeron.workspace.WorkspaceUserFixture.WORKSPACE_USER_WITH_TEAM;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TeamCommandServiceTest {

    @Mock TeamCommandPort teamCommandPort;
    @Mock WorkspaceQueryPort workspaceQueryPort;
    @Mock TeamQueryPort teamQueryPort;
    @Mock WorkspaceUserQueryPort workspaceUserQueryPort;
    @InjectMocks TeamCommandService teamCommandService;

    private Workspace workspace;
    private Team team;
    private WorkspaceUser workspaceUser1;
    private WorkspaceUser workspaceUser2;

    @BeforeEach
    void setUp() {
        workspace = WORKSPACE_1;
        team = TEAM_1;
        workspaceUser1 = WORKSPACE_USER_WITH_TEAM;
        workspaceUser2 = WORKSPACE_USER_ADMIN;
    }

    @DisplayName("팀 생성 - 실패 / 존재하지 않는 워크스페이스의 경우")
    @Test
    void create_team_fail_not_found_workspace() throws Exception {

        // given
        CreateTeamRequestDto requestDto = createCreateTeamRequestDto();
        when(workspaceQueryPort.findById(any()))
                .thenReturn(Optional.empty());

        // when, then
        assertThrows(WorkspaceNotFoundException.class,
                () -> teamCommandService.createTeam(requestDto));
    }

    @DisplayName("팀 생성 - 실패 / 존재하지 않는 워크스페이스의 경우")
    @Test
    void create_team_fail_not_over_five_teams() throws Exception {

        // given
        CreateTeamRequestDto requestDto = createCreateTeamRequestDto();
        when(workspaceQueryPort.findById(any()))
                .thenReturn(Optional.of(workspace));
        when(teamQueryPort.countByWorkspaceId(any()))
                .thenReturn(5L);

        // when, then
        assertThrows(TeamCountsConditionException.class,
                () -> teamCommandService.createTeam(requestDto));
    }

    @DisplayName("팀 생성 - 성공")
    @Test
    void create_team_success() throws Exception {

        // given
        CreateTeamRequestDto requestDto = createCreateTeamRequestDto();
        when(workspaceQueryPort.findById(requestDto.getWorkspaceId()))
                .thenReturn(Optional.of(workspace));
        when(teamQueryPort.countByWorkspaceId(any()))
                .thenReturn(4L);
        when(teamCommandPort.save(any()))
                .thenReturn(team.getId());

        // when
        Long teamId = teamCommandService.createTeam(requestDto);

        // then
        assertAll(
                () -> verify(workspaceQueryPort).findById(requestDto.getWorkspaceId()),
                () -> verify(teamQueryPort).countByWorkspaceId(requestDto.getWorkspaceId()),
                () -> verify(teamCommandPort).save(any(Team.class)),
                () -> assertEquals(team.getId(), teamId)
        );
    }

    private CreateTeamRequestDto createCreateTeamRequestDto() {
        return CreateTeamRequestDto.builder()
                .teamName("테스트")
                .workspaceId(1L)
                .build();
    }

    @DisplayName("팀 삭제 - 성공")
    @Test
    void delete_team_success() throws Exception {

        // given
        DeleteTeamRequestDto requestDto = DeleteTeamRequestDtoBuilder.build();
        when(workspaceUserQueryPort.findByTeamId(any()))
                .thenReturn(List.of(workspaceUser2));

        // when
        teamCommandService.deleteTeam(requestDto);

        // then
        assertAll(
                () -> verify(teamCommandPort).deleteById(requestDto.getTeamId()),
                () -> assertNull(workspaceUser2.getTeam())
        );
    }

    @DisplayName("팀명 바꾸기 - 실패 / 팀이 존재하지 않을 경우")
    @Test
    void change_team_name_fail_not_found_team() throws Exception {

        // given
        ModifyTeamNameRequestDto requestDto = SetTeamNameRequestDtoBuilder.build();
        when(teamQueryPort.findById(any()))
                .thenReturn(Optional.empty());

        // when, then
        assertThrows(TeamNotFoundException.class,
                () -> teamCommandService.modifyTeamName(requestDto));
    }

    @DisplayName("팀명 바꾸기 - 성공")
    @Test
    void change_team_name_success() throws Exception {

        // given
        ModifyTeamNameRequestDto requestDto = SetTeamNameRequestDtoBuilder.build();
        when(teamQueryPort.findById(any()))
                .thenReturn(Optional.of(team));

        // when
        teamCommandService.modifyTeamName(requestDto);

        // then
        assertAll(
                () -> verify(teamQueryPort).findById(requestDto.getTeamId()),
                () -> assertEquals(requestDto.getName(), team.getName())
        );
    }
}
