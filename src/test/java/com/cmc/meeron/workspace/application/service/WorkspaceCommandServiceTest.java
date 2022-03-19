package com.cmc.meeron.workspace.application.service;

import com.cmc.meeron.workspace.application.port.in.response.WorkspaceResponseDto;
import com.cmc.meeron.workspace.application.port.out.WorkspaceCommandPort;
import com.cmc.meeron.workspace.domain.Workspace;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.cmc.meeron.workspace.WorkspaceFixture.WORKSPACE_1;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WorkspaceCommandServiceTest {

    @Mock WorkspaceCommandPort workspaceCommandPort;
    @InjectMocks WorkspaceCommandService workspaceCommandService;

    private Workspace workspace;

    @BeforeEach
    void setUp() {
        workspace = WORKSPACE_1;
    }

    @DisplayName("워크스페이스 생성 - 성공")
    @Test
    void create_workspace_success() throws Exception {

        // given
        String name = "테스트";
        when(workspaceCommandPort.save(any()))
                .thenReturn(workspace);

        // when
        WorkspaceResponseDto responseDto = workspaceCommandService.createWorkspace(name);

        // then
        assertAll(
                () -> assertEquals(workspace.getId(), responseDto.getWorkspaceId()),
                () -> assertEquals(workspace.getName(), responseDto.getWorkspaceName()),
                () -> assertEquals(workspace.getWorkspaceLogoUrl(), responseDto.getWorkspaceLogoUrl()),
                () -> verify(workspaceCommandPort).save(any(Workspace.class))
        );
    }
}