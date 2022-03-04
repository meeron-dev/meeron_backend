package com.cmc.meeron.workspace.application;

import com.cmc.meeron.common.exception.workspace.WorkspaceNotFoundException;
import com.cmc.meeron.workspace.application.dto.response.WorkspaceResponseDto;
import com.cmc.meeron.workspace.domain.Workspace;
import com.cmc.meeron.workspace.domain.WorkspaceRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class WorkspaceServiceTest {

    @Mock WorkspaceRepository workspaceRepository;
    @InjectMocks WorkspaceService workspaceService;

    @DisplayName("유저의 워크스페이스 조회 - 성공")
    @Test
    void get_my_workspaces_success() throws Exception {

        // given
        List<Workspace> myWorkspaces = createMyWorkspaces();
        when(workspaceRepository.findMyWorkspaces(any()))
                .thenReturn(myWorkspaces);

        // when
        List<WorkspaceResponseDto> workspaceResponseDtos = workspaceService.getMyWorkspaces(1L);

        // then
        assertAll(
                () -> verify(workspaceRepository).findMyWorkspaces(1L),
                () -> assertEquals(myWorkspaces.get(0).getId(), workspaceResponseDtos.get(0).getWorkspaceId()),
                () -> assertEquals(myWorkspaces.get(1).getId(), workspaceResponseDtos.get(1).getWorkspaceId())
        );
    }

    private List<Workspace> createMyWorkspaces() {
        return List.of(
                createWorkspace(),
                Workspace.builder()
                        .id(2L)
                        .name("테스트 워크스페이스2")
                        .workspaceLogoUrl("https://image.url.com/123124")
                        .build()
        );
    }

    private Workspace createWorkspace() {
        return Workspace.builder()
                .id(1L)
                .name("테스트 워크스페이스1")
                .workspaceLogoUrl("https://image.url.com/123123")
                .build();
    }

    @DisplayName("워크스페이스 단일 조회 - 성공")
    @Test
    void get_workspace_success() throws Exception {

        // given
        Workspace workspace = createWorkspace();
        when(workspaceRepository.findById(any()))
                .thenReturn(Optional.of(workspace));

        // when
        WorkspaceResponseDto response = workspaceService.getWorkspace(workspace.getId());

        // then
        assertAll(
                () -> verify(workspaceRepository).findById(workspace.getId()),
                () -> assertEquals(workspace.getId(), response.getWorkspaceId())
        );
    }

    @DisplayName("워크스페이스 단일 조회 - 실패 / 존재하지 않는 워크스페이스일 경우")
    @Test
    void get_workspace_fail_not_found_workspace() throws Exception {

        // given
        when(workspaceRepository.findById(any()))
                .thenReturn(Optional.empty());

        // when, then
        assertThrows(WorkspaceNotFoundException.class,
                () -> workspaceService.getWorkspace(1L));
    }
}
