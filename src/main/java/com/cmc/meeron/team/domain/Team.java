package com.cmc.meeron.team.domain;

import com.cmc.meeron.common.domain.BaseEntity;
import com.cmc.meeron.workspace.domain.Workspace;
import lombok.*;

import javax.persistence.*;

@Entity
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
}
