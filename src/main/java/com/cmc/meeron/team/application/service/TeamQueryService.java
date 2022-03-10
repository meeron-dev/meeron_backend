package com.cmc.meeron.team.application.service;

import com.cmc.meeron.team.application.port.in.response.WorkspaceTeamsResponseDto;
import com.cmc.meeron.team.application.port.in.TeamQueryUseCase;
import com.cmc.meeron.team.application.port.out.TeamQueryPort;
import com.cmc.meeron.team.application.port.out.response.WorkspaceTeamsQueryResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
class TeamQueryService implements TeamQueryUseCase {

    private final TeamQueryPort teamQueryPort;

    @Override
    public List<WorkspaceTeamsResponseDto> getWorkspaceTeams(Long workspaceId) {
        List<WorkspaceTeamsQueryResponseDto> workspaceTeamsQueryResponseDtos = teamQueryPort
                .findByWorkspaceId(workspaceId);
        return WorkspaceTeamsResponseDto.fromQueryResponseDtos(workspaceTeamsQueryResponseDtos);
    }
}
