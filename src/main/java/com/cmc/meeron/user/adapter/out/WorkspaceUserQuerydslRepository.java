package com.cmc.meeron.user.adapter.out;

import com.cmc.meeron.user.application.port.out.response.QWorkspaceUserQueryResponseDto;
import com.cmc.meeron.user.application.port.out.response.WorkspaceUserQueryResponseDto;
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
                workspaceUser.workspace.id, workspaceUser.id, workspaceUser.profileImageUrl,
                workspaceUser.nickname, workspaceUser.position, workspaceUser.isWorkspaceAdmin))
                .from(workspaceUser)
                .where(workspaceUser.workspace.id.eq(workspaceId),
                        workspaceUser.nickname.startsWith(nickname))
                .orderBy(workspaceUser.nickname.asc())
                .fetch();
    }

    public List<WorkspaceUserQueryResponseDto> findByTeamId(Long teamId) {
        return queryFactory.select(new QWorkspaceUserQueryResponseDto(
                workspaceUser.workspace.id, workspaceUser.id, workspaceUser.profileImageUrl,
                workspaceUser.nickname, workspaceUser.position, workspaceUser.isWorkspaceAdmin))
                .from(teamUser)
                .join(teamUser.workspaceUser, workspaceUser)
                .where(teamUser.team.id.eq(teamId))
                .orderBy(workspaceUser.nickname.asc())
                .fetch();
    }
}
