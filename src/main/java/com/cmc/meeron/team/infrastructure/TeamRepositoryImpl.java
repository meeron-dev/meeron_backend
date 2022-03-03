package com.cmc.meeron.team.infrastructure;

import com.cmc.meeron.team.domain.TeamRepository;
import com.cmc.meeron.team.domain.dto.WorkspaceTeamsQueryResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
class TeamRepositoryImpl implements TeamRepository {

    private final TeamQuerydslRepository teamQuerydslRepository;

    @Override
    public List<WorkspaceTeamsQueryResponseDto> findByWorkspaceId(Long workspaceId) {
        return teamQuerydslRepository.findTeamsByWorkspaceId(workspaceId);
    }
}
