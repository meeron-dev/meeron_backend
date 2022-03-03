package com.cmc.meeron.team.infrastructure;

import com.cmc.meeron.team.domain.dto.QWorkspaceTeamsQueryResponseDto;
import com.cmc.meeron.team.domain.dto.WorkspaceTeamsQueryResponseDto;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.cmc.meeron.team.domain.QTeam.team;

@Repository
@RequiredArgsConstructor
class TeamQuerydslRepository {

    private final JPAQueryFactory queryFactory;

    public List<WorkspaceTeamsQueryResponseDto> findTeamsByWorkspaceId(Long workspaceId) {
        return queryFactory.select(new QWorkspaceTeamsQueryResponseDto(team.id, team.name))
                .from(team)
                .where(team.workspace.id.eq(workspaceId))
                .orderBy(team.id.desc())
                .fetch();
    }
}
