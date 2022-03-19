package com.cmc.meeron.team.application.service;

import com.cmc.meeron.common.exception.workspace.WorkspaceNotFoundException;
import com.cmc.meeron.team.application.port.in.request.CreateTeamRequestDto;
import com.cmc.meeron.team.application.port.out.TeamCommandPort;
import com.cmc.meeron.team.domain.Team;
import com.cmc.meeron.workspace.application.port.out.WorkspaceQueryPort;
import com.cmc.meeron.workspace.domain.Workspace;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static com.cmc.meeron.team.TeamFixture.TEAM;
import static com.cmc.meeron.workspace.WorkspaceFixture.WORKSPACE_1;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TeamCommandServiceTest {

    @Mock TeamCommandPort teamCommandPort;
    @Mock WorkspaceQueryPort workspaceQueryPort;
    @InjectMocks TeamCommandService teamCommandService;

    private Workspace workspace;
    private Team team;

    @BeforeEach
    void setUp() {
        workspace = WORKSPACE_1;
        team = TEAM;
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

    @DisplayName("팀 생성 - 성공")
    @Test
    void create_team_success() throws Exception {

        // given
        CreateTeamRequestDto requestDto = createCreateTeamRequestDto();
        when(workspaceQueryPort.findById(requestDto.getWorkspaceId()))
                .thenReturn(Optional.of(workspace));
        when(teamCommandPort.save(any()))
                .thenReturn(team.getId());

        // when
        Long teamId = teamCommandService.createTeam(requestDto);

        // then
        assertAll(
                () -> verify(workspaceQueryPort).findById(requestDto.getWorkspaceId()),
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
}
