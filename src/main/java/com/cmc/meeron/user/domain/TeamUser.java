package com.cmc.meeron.user.domain;

import com.cmc.meeron.common.domain.BaseEntity;
import com.cmc.meeron.team.domain.Team;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class TeamUser extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "TEAM_USER_ID")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "TEAM_ID", nullable = false)
    private Team team;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "WORKSPACE_USER_ID", nullable = false)
    private WorkspaceUser workspaceUser;

    @Column(nullable = false, columnDefinition = "TINYINT")
    private boolean isTeamAdmin;
}
