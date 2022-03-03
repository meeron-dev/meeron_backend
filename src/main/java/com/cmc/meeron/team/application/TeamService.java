package com.cmc.meeron.team.application;

import com.cmc.meeron.team.application.dto.response.WorkspaceTeamsResponseDto;
import com.cmc.meeron.team.domain.TeamRepository;
import com.cmc.meeron.team.domain.dto.WorkspaceTeamsQueryResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
class TeamService implements TeamQueryUseCase {

    private final TeamRepository teamRepository;

    @Override
    public List<WorkspaceTeamsResponseDto> getWorkspaceTeams(Long workspaceId) {
        List<WorkspaceTeamsQueryResponseDto> workspaceTeamsQueryResponseDtos = teamRepository
                .findByWorkspaceId(workspaceId);
        return WorkspaceTeamsResponseDto.ofList(workspaceTeamsQueryResponseDtos);
    }
}
