package com.cmc.meeron.team.application;

import com.cmc.meeron.team.application.dto.response.WorkspaceTeamsResponseDto;
import com.cmc.meeron.team.domain.TeamRepository;
import com.cmc.meeron.team.domain.dto.WorkspaceTeamsQueryResponseDto;
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
class TeamServiceTest {

    @Mock TeamRepository teamRepository;
    @InjectMocks TeamService teamService;

    @DisplayName("워크스페이스 내 팀 조회 - 성공")
    @Test
    void get_workspace_teams_success() throws Exception {

        // given
        List<WorkspaceTeamsQueryResponseDto> responseQueryDtos = createWorkspaceTeamsQueryResponseDtos();
        when(teamRepository.findByWorkspaceId(any()))
                .thenReturn(responseQueryDtos);

        // when
        List<WorkspaceTeamsResponseDto> workspaceTeams = teamService.getWorkspaceTeams(1L);

        // then
        assertAll(
                () -> verify(teamRepository).findByWorkspaceId(1L),
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