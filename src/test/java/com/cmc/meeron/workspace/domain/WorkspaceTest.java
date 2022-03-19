package com.cmc.meeron.workspace.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class WorkspaceTest {

    @DisplayName("워크스페이스 생성 - 성공")
    @Test
    void of_success() throws Exception {

        // given
        String name = "테스트";

        // when
        Workspace workspace = Workspace.of(name);

        // then
        assertAll(
                () -> assertNotNull(workspace.getWorkspaceLogoUrl()),
                () -> assertEquals(name, workspace.getName())
        );
    }
}