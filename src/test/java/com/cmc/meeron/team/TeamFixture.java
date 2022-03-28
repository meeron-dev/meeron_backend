package com.cmc.meeron.team;

import com.cmc.meeron.team.domain.Team;

import static com.cmc.meeron.workspace.WorkspaceFixture.WORKSPACE_1;

public class TeamFixture {

    public static final Team TEAM_1 = Team.builder()
            .id(5L)
            .workspace(WORKSPACE_1)
            .name("테스트팀")
            .build();
}
