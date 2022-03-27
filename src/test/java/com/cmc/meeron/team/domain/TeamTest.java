package com.cmc.meeron.team.domain;

import com.cmc.meeron.common.exception.meeting.NotWorkspacesTeamException;
import com.cmc.meeron.workspace.domain.Workspace;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.cmc.meeron.team.TeamFixture.TEAM_1;
import static com.cmc.meeron.workspace.WorkspaceFixture.WORKSPACE_1;
import static com.cmc.meeron.workspace.WorkspaceFixture.WORKSPACE_2;
import static org.junit.jupiter.api.Assertions.*;

class TeamTest {

    private Workspace workspace;
    private Team team;

    @BeforeEach
    void setUp() {
        workspace = WORKSPACE_1;
        team = TEAM_1;
    }

    @DisplayName("팀 생성 - 성공")
    @Test
    void of_success() throws Exception {

        // given
        String name = "테스트";

        // when
        Team team = Team.of(workspace, name);

        // then
        assertAll(
                () -> assertEquals(name, team.getName()),
                () -> assertEquals(workspace, team.getWorkspace()),
                () -> assertNotNull(team.getTeamLogoUrl())
        );
    }

    @DisplayName("팀이 워크스페이스에 속해있는지 검증 - 실패")
    @Test
    void valid_workspace_fail() throws Exception {

        // given
        Workspace target = WORKSPACE_2;

        // when, then
        assertThrows(NotWorkspacesTeamException.class,
                () -> team.validWorkspace(target));
    }

    @DisplayName("팀이 워크스페이스에 속해있는지 검증 - 성공")
    @Test
    void valid_workspace_success() throws Exception {

        // given, when, then
        assertDoesNotThrow(() -> team.validWorkspace(workspace));
    }
}
