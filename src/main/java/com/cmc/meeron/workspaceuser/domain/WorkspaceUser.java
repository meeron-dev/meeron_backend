package com.cmc.meeron.workspaceuser.domain;

import com.cmc.meeron.common.domain.BaseEntity;
import com.cmc.meeron.common.exception.team.PreviousBelongToTeamException;
import com.cmc.meeron.common.exception.workspace.NotAdminException;
import com.cmc.meeron.common.exception.workspace.WorkspaceUsersNotInEqualWorkspaceException;
import com.cmc.meeron.team.domain.Team;
import com.cmc.meeron.user.domain.User;
import com.cmc.meeron.workspace.domain.Workspace;
import lombok.*;
import org.hibernate.annotations.Where;

import javax.persistence.*;

@Entity
@Where(clause = "DELETED=false")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class WorkspaceUser extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "WORKSPACE_USER_ID")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "WORKSPACE_ID", nullable = false)
    private Workspace workspace;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "TEAM_ID")
    private Team team;

    @Embedded
    private WorkspaceUserInfo workspaceUserInfo;

    @Column(nullable = false, columnDefinition = "TINYINT")
    private boolean deleted;

    public static WorkspaceUser of(User user,
                                   Workspace workspace,
                                   WorkspaceUserInfo info) {
        return WorkspaceUser.builder()
                .user(user)
                .workspace(workspace)
                .workspaceUserInfo(info)
                .build();
    }

    public void validInWorkspace(Workspace workspace) {
        if (!this.workspace.equals(workspace)) {
            throw new WorkspaceUsersNotInEqualWorkspaceException();
        }
    }

    public void joinTeam(Team team) {
        if (this.team != null) {
            throw new PreviousBelongToTeamException();
        }
        this.team = team;
    }

    public void exitTeam() {
        team = null;
    }

    public void isAdminOrThrow() {
        if (!workspaceUserInfo.isWorkspaceAdmin()) {
            throw new NotAdminException();
        }
    }

    public void modifyInfo(WorkspaceUserInfo workspaceUserInfo) {
        this.workspaceUserInfo = workspaceUserInfo;
    }

    public void quit() {
        deleted = true;
        if (workspaceUserInfo.isWorkspaceAdmin()) {
            workspace.delete();
        }
    }

    public boolean isAdmin() {
        return workspaceUserInfo.isWorkspaceAdmin();
    }
}
