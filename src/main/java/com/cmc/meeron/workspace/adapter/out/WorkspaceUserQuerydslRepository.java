package com.cmc.meeron.workspace.adapter.out;

import com.cmc.meeron.workspace.application.port.out.response.QWorkspaceUserQueryResponseDto;
import com.cmc.meeron.workspace.application.port.out.response.WorkspaceUserQueryResponseDto;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.cmc.meeron.team.domain.QTeamUser.teamUser;
import static com.cmc.meeron.workspace.domain.QWorkspaceUser.workspaceUser;


@Repository
@RequiredArgsConstructor
class WorkspaceUserQuerydslRepository {

    private final JPAQueryFactory queryFactory;

    public List<WorkspaceUserQueryResponseDto> findByWorkspaceIdNickname(Long workspaceId, String nickname) {
        return queryFactory.select(new QWorkspaceUserQueryResponseDto(
                workspaceUser.workspace.id, workspaceUser.id, workspaceUser.workspaceUserInfo.profileImageUrl,
                workspaceUser.workspaceUserInfo.nickname, workspaceUser.workspaceUserInfo.position, workspaceUser.workspaceUserInfo.isWorkspaceAdmin,
                workspaceUser.workspaceUserInfo.contactMail))
                .from(workspaceUser)
                .where(workspaceUser.workspace.id.eq(workspaceId),
                        workspaceUser.workspaceUserInfo.nickname.startsWith(nickname))
                .orderBy(workspaceUser.workspaceUserInfo.nickname.asc())
                .fetch();
    }

    public List<WorkspaceUserQueryResponseDto> findByTeamId(Long teamId) {
        return queryFactory.select(new QWorkspaceUserQueryResponseDto(
                workspaceUser.workspace.id, workspaceUser.id, workspaceUser.workspaceUserInfo.profileImageUrl,
                workspaceUser.workspaceUserInfo.nickname, workspaceUser.workspaceUserInfo.position, workspaceUser.workspaceUserInfo.isWorkspaceAdmin,
                workspaceUser.workspaceUserInfo.contactMail))
                .from(teamUser)
                .join(teamUser.workspaceUser, workspaceUser)
                .where(teamUser.team.id.eq(teamId))
                .orderBy(workspaceUser.workspaceUserInfo.nickname.asc())
                .fetch();
    }

    public boolean existsByNicknameInWorkspace(Long workspaceId, String nickname) {
        Integer exists = queryFactory.selectOne()
                .from(workspaceUser)
                .where(workspaceUser.workspace.id.eq(workspaceId),
                        workspaceUser.workspaceUserInfo.nickname.eq(nickname))
                .fetchFirst();
        return exists != null;
    }
}
