package com.cmc.meeron.team.domain;

import com.cmc.meeron.common.domain.BaseEntity;
import com.cmc.meeron.common.exception.meeting.NotWorkspacesTeamException;
import com.cmc.meeron.workspace.domain.Workspace;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;

@Entity
@SQLDelete(sql = "UPDATE TEAM SET DELETED = true WHERE TEAM_ID=?")
@Where(clause = "DELETED=false")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class Team extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "TEAM_ID")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "WORKSPACE_ID", nullable = false)
    private Workspace workspace;

    @Column(nullable = false, length = 30)
    private String name;

    @Column(length = 200)
    private String teamLogoUrl;

    @Column(nullable = false, columnDefinition = "TINYINT")
    private boolean deleted;

    public static Team of(Workspace workspace, String name) {
        return Team.builder()
                .workspace(workspace)
                .name(name)
                .teamLogoUrl("")
                .deleted(false)
                .build();
    }

    public void validWorkspace(Workspace target) {
        if (!workspace.equals(target)) {
            throw new NotWorkspacesTeamException();
        }
    }

    public void setName(String name) {
        this.name = name;
    }
}
