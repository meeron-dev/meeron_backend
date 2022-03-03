package com.cmc.meeron.user.infrastructure;

import com.cmc.meeron.user.domain.dto.QWorkspaceUserQueryResponseDto;
import com.cmc.meeron.user.domain.dto.WorkspaceUserQueryResponseDto;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.cmc.meeron.user.domain.QTeamUser.teamUser;
import static com.cmc.meeron.user.domain.QWorkspaceUser.workspaceUser;

@Repository
@RequiredArgsConstructor
class WorkspaceUserQuerydslRepository {

    private final JPAQueryFactory queryFactory;

    public List<WorkspaceUserQueryResponseDto> findByWorkspaceIdNickname(Long workspaceId, String nickname) {
        return queryFactory.select(new QWorkspaceUserQueryResponseDto(
                workspaceUser.id, workspaceUser.profileImageUrl, workspaceUser.nickname, workspaceUser.position))
                .from(workspaceUser)
                .where(workspaceUser.workspace.id.eq(workspaceId),
                        workspaceUser.nickname.startsWith(nickname))
                .orderBy(workspaceUser.nickname.asc())
                .fetch();
    }

    public List<WorkspaceUserQueryResponseDto> findByTeamId(Long teamId) {
        return queryFactory.select(new QWorkspaceUserQueryResponseDto(
                workspaceUser.id, workspaceUser.profileImageUrl, workspaceUser.nickname, workspaceUser.position))
                .from(teamUser)
                .join(teamUser.workspaceUser, workspaceUser)
                .where(teamUser.team.id.eq(teamId))
                .orderBy(workspaceUser.nickname.asc())
                .fetch();
    }
}
