package com.cmc.meeron.user.domain;

import com.cmc.meeron.common.domain.BaseEntity;
import com.cmc.meeron.common.exception.workspace.WorkspaceUsersNotInEqualWorkspaceException;
import com.cmc.meeron.workspace.domain.Workspace;
import lombok.*;

import javax.persistence.*;

@Entity
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

    @Embedded
    private WorkspaceUserInfo workspaceUserInfo;

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
}
