package com.cmc.meeron.workspace.domain;

import com.cmc.meeron.common.domain.BaseEntity;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class Workspace extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "WORKSPACE_ID")
    private Long id;

    @Column(length = 50, nullable = false)
    private String name;

    @Column(length = 200)
    private String workspaceLogoUrl;

    public static Workspace of(String name) {
        return Workspace.builder()
                .name(name)
                .workspaceLogoUrl("")
                .build();
    }
}
