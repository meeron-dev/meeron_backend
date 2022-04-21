package com.cmc.meeron.team.application.service;

import com.cmc.meeron.common.exception.team.TeamNotFoundException;
import com.cmc.meeron.team.application.port.in.response.TeamResponseDto;
import com.cmc.meeron.team.application.port.in.response.WorkspaceTeamsResponseDto;
import com.cmc.meeron.team.application.port.out.TeamQueryPort;
import com.cmc.meeron.team.application.port.out.response.WorkspaceTeamsQueryResponseDto;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static com.cmc.meeron.team.TeamFixture.TEAM_1;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TeamQueryServiceTest {

    @Mock
    TeamQueryPort teamQueryPort;
    @InjectMocks
    TeamQueryService teamQueryService;

    @DisplayName("워크스페이스 내 팀 조회 - 성공")
    @Test
    void get_workspace_teams_success() throws Exception {

        // given
        List<WorkspaceTeamsQueryResponseDto> responseQueryDtos = createWorkspaceTeamsQueryResponseDtos();
        when(teamQueryPort.findByWorkspaceId(any()))
                .thenReturn(responseQueryDtos);

        // when
        List<WorkspaceTeamsResponseDto> workspaceTeams = teamQueryService.getWorkspaceTeams(1L);

        // then
        assertAll(
                () -> verify(teamQueryPort).findByWorkspaceId(1L),
                () -> assertEquals(responseQueryDtos.size(), workspaceTeams.size())
        );
    }

    private List<WorkspaceTeamsQueryResponseDto> createWorkspaceTeamsQueryResponseDtos() {
        return List.of(
                WorkspaceTeamsQueryResponseDto.builder().teamId(1L).teamName("첫번째 팀").build(),
                WorkspaceTeamsQueryResponseDto.builder().teamId(2L).teamName("두번째 팀").build()
        );
    }

    @DisplayName("회의를 주관하는 팀 실패 - 존재하지 않을 경우")
    @Test
    void get_meeting_host_team_fail_not_found() throws Exception {

        // given
        when(teamQueryPort.findByMeetingId(any()))
                .thenReturn(Optional.empty());

        // when, then
        assertThrows(TeamNotFoundException.class,
                () -> teamQueryService.getMeetingHostTeam(1L));
    }

    @DisplayName("회의를 주관하는 팀 조회 - 성공")
    @Test
    void get_meeting_host_team_success() throws Exception {

        // given
        when(teamQueryPort.findByMeetingId(any()))
                .thenReturn(Optional.of(TEAM_1));

        // when
        TeamResponseDto responseDto = teamQueryService.getMeetingHostTeam(1L);

        // then
        assertAll(
                () -> verify(teamQueryPort).findByMeetingId(1L),
                () -> Assertions.assertThat(responseDto)
                        .usingRecursiveComparison()
                        .isEqualTo(TeamResponseDto.from(TEAM_1))
        );
    }
}
