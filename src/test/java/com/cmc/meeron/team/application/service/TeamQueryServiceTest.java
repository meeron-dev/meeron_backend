package com.cmc.meeron.team.application.service;

import com.cmc.meeron.team.application.port.in.response.WorkspaceTeamsResponseDto;
import com.cmc.meeron.team.application.port.out.TeamQueryPort;
import com.cmc.meeron.team.application.port.out.response.WorkspaceTeamsQueryResponseDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
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

}