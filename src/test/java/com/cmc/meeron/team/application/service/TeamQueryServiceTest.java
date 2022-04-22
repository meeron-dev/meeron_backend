package com.cmc.meeron.team.application.service;

import com.cmc.meeron.common.exception.team.TeamNotFoundException;
import com.cmc.meeron.team.application.port.in.response.TeamResponseDto;
import com.cmc.meeron.team.application.port.out.TeamQueryPort;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static com.cmc.meeron.team.TeamFixture.TEAM_1;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TeamQueryServiceTest {

    @Mock
    TeamQueryPort teamQueryPort;
    @InjectMocks
    TeamQueryService teamQueryService;

    @DisplayName("워크스페이스의 모든 팀 조회 - 성공")
    @Test
    void get_workspace_teams_success() throws Exception {

        // given
        when(teamQueryPort.findByWorkspaceId(any()))
                .thenReturn(List.of(TEAM_1));

        // when
        List<TeamResponseDto> responseDtos = teamQueryService.getWorkspaceTeams(1L);

        // then
        assertAll(
                () -> verify(teamQueryPort).findByWorkspaceId(1L),
                () -> assertThat(responseDtos)
                        .usingRecursiveComparison()
                        .isEqualTo(TeamResponseDto.from(List.of(TEAM_1)))
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
                () -> assertThat(responseDto)
                        .usingRecursiveComparison()
                        .isEqualTo(TeamResponseDto.from(TEAM_1))
        );
    }
}
