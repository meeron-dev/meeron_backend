package com.cmc.meeron.team.adapter.out;

import com.cmc.meeron.team.application.port.out.TeamQueryPort;
import com.cmc.meeron.team.application.port.out.response.WorkspaceTeamsQueryResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
class TeamQueryRepository implements TeamQueryPort {

    private final TeamQuerydslRepository teamQuerydslRepository;

    @Override
    public List<WorkspaceTeamsQueryResponseDto> findByWorkspaceId(Long workspaceId) {
        return teamQuerydslRepository.findTeamsByWorkspaceId(workspaceId);
    }
}
