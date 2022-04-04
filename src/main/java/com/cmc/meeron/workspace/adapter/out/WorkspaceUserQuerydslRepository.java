package com.cmc.meeron.workspace.adapter.out;

import com.cmc.meeron.workspace.application.port.out.response.QWorkspaceUserQuerydslResponseDto;
import com.cmc.meeron.workspace.application.port.out.response.WorkspaceUserQuerydslResponseDto;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.cmc.meeron.team.domain.QTeam.team;
import static com.cmc.meeron.workspace.domain.QWorkspaceUser.workspaceUser;


@Repository
@RequiredArgsConstructor
class WorkspaceUserQuerydslRepository {

    private final JPAQueryFactory queryFactory;

    public List<WorkspaceUserQuerydslResponseDto> findByWorkspaceIdNickname(Long workspaceId, String nickname) {
        return queryFactory.select(new QWorkspaceUserQuerydslResponseDto(
                workspaceUser.workspace.id, workspaceUser.id, workspaceUser.workspaceUserInfo.profileImageUrl,
                workspaceUser.workspaceUserInfo.nickname, workspaceUser.workspaceUserInfo.position, workspaceUser.workspaceUserInfo.isWorkspaceAdmin,
                workspaceUser.workspaceUserInfo.contactMail, workspaceUser.workspaceUserInfo.phone))
                .from(workspaceUser)
                .where(workspaceUser.workspace.id.eq(workspaceId),
                        workspaceUser.workspaceUserInfo.nickname.startsWith(nickname))
                .fetch();
    }

    public List<WorkspaceUserQuerydslResponseDto> findByTeamId(Long teamId) {
        return queryFactory.select(new QWorkspaceUserQuerydslResponseDto(
                workspaceUser.workspace.id, workspaceUser.id, workspaceUser.workspaceUserInfo.profileImageUrl,
                workspaceUser.workspaceUserInfo.nickname, workspaceUser.workspaceUserInfo.position, workspaceUser.workspaceUserInfo.isWorkspaceAdmin,
                workspaceUser.workspaceUserInfo.contactMail, workspaceUser.workspaceUserInfo.phone))
                .from(workspaceUser)
                .join(workspaceUser.team, team)
                .where(team.id.eq(teamId))
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
