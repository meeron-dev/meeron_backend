package com.cmc.meeron.workspace.application.service;

import com.cmc.meeron.common.exception.user.NicknameDuplicateException;
import com.cmc.meeron.common.exception.user.UserNotFoundException;
import com.cmc.meeron.common.exception.workspace.WorkspaceNotFoundException;
import com.cmc.meeron.file.application.port.in.FileManager;
import com.cmc.meeron.user.application.port.out.UserQueryPort;
import com.cmc.meeron.user.domain.User;
import com.cmc.meeron.workspace.application.port.in.request.CreateWorkspaceUserRequestDto;
import com.cmc.meeron.workspace.application.port.in.response.WorkspaceUserResponseDto;
import com.cmc.meeron.workspace.application.port.out.WorkspaceQueryPort;
import com.cmc.meeron.workspace.application.port.out.WorkspaceUserCommandPort;
import com.cmc.meeron.workspace.application.port.out.WorkspaceUserQueryPort;
import com.cmc.meeron.workspace.domain.Workspace;
import com.cmc.meeron.workspace.domain.WorkspaceUser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static com.cmc.meeron.file.FileFixture.FILE;
import static com.cmc.meeron.user.UserFixture.USER;
import static com.cmc.meeron.workspace.WorkspaceFixture.WORKSPACE_1;
import static com.cmc.meeron.workspace.WorkspaceUserFixture.WORKSPACE_USER_1;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WorkspaceUserCommandServiceTest {

    @Mock UserQueryPort userQueryPort;
    @Mock WorkspaceQueryPort workspaceQueryPort;
    @Mock WorkspaceUserQueryPort workspaceUserQueryPort;
    @Mock WorkspaceUserCommandPort workspaceUserCommandPort;
    @Mock FileManager fileManager;
    @InjectMocks WorkspaceUserCommandService workspaceUserCommandService;

    private Workspace workspace;
    private User user;
    private WorkspaceUser workspaceUser;

    @BeforeEach
    void setUp() {
        user = USER;
        workspace = WORKSPACE_1;
        workspaceUser = WORKSPACE_USER_1;
    }

    @DisplayName("워크스페이스 유저 생성 - 실패 / 닉네임이 중복되었을 경우")
    @Test
    void create_workspace_user_fail_valid_nickname() throws Exception {

        // given
        CreateWorkspaceUserRequestDto requestDto = createCreateWorkspaceUserRequestDto();
        when(workspaceUserQueryPort.existsByNicknameInWorkspace(any(), any()))
                .thenReturn(true);

        // when, then
        assertThrows(NicknameDuplicateException.class,
                () -> workspaceUserCommandService.createWorkspaceUser(requestDto));
    }

    private CreateWorkspaceUserRequestDto createCreateWorkspaceUserRequestDto() {
        return CreateWorkspaceUserRequestDto.builder()
                .workspaceId(1L)
                .profileImage(FILE)
                .nickname("테스트닉네임")
                .position("테스트직책")
                .email("test@test.com")
                .phone("010-1234-1234")
                .isAdmin(true)
                .build();
    }

    @DisplayName("워크스페이스 유저 생성 - 실패 / 존재하지 않는 워크스페이스일 경우")
    @Test
    void create_workspace_user_fail_not_found_workspace() throws Exception {

        // given
        CreateWorkspaceUserRequestDto requestDto = createCreateWorkspaceUserRequestDto();
        when(userQueryPort.findById(any()))
                .thenReturn(Optional.of(user));
        when(workspaceQueryPort.findById(any()))
                .thenReturn(Optional.empty());

        // when, then
        assertThrows(WorkspaceNotFoundException.class,
                () -> workspaceUserCommandService.createWorkspaceUser(requestDto));
    }

    @DisplayName("워크스페이스 유저 생성 - 실패 / 존재하지 않는 유저일 경우")
    @Test
    void create_workspace_user_fail_not_found_user() throws Exception {

        // given
        CreateWorkspaceUserRequestDto requestDto = createCreateWorkspaceUserRequestDto();
        when(userQueryPort.findById(any()))
                .thenReturn(Optional.empty());

        // when, then
        assertThrows(UserNotFoundException.class,
                () -> workspaceUserCommandService.createWorkspaceUser(requestDto));
    }

    @DisplayName("워크스페이스 유저 생성 - 성공")
    @Test
    void create_workspace_user_success() throws Exception {

        // given
        CreateWorkspaceUserRequestDto requestDto = createCreateWorkspaceUserRequestDto();
        saveWorkspaceUserProcess();
        when(fileManager.saveProfileImage(any()))
                .thenReturn(FILE.getOriginalFilename());

        // when
        WorkspaceUserResponseDto responseDto = workspaceUserCommandService.createWorkspaceUser(requestDto);

        // then
        assertAll(
                () -> verify(workspaceUserQueryPort).existsByNicknameInWorkspace(requestDto.getWorkspaceId(), requestDto.getNickname()),
                () -> verify(userQueryPort).findById(requestDto.getUserId()),
                () -> verify(workspaceQueryPort).findById(requestDto.getWorkspaceId()),
                () -> verify(fileManager).saveProfileImage(any()),
                () -> verify(workspaceUserCommandPort).saveWorkspaceUser(any(WorkspaceUser.class))
        );
    }

    private void saveWorkspaceUserProcess() {
        when(workspaceUserQueryPort.existsByNicknameInWorkspace(any(), any()))
                .thenReturn(false);
        when(userQueryPort.findById(any()))
                .thenReturn(Optional.of(user));
        when(workspaceQueryPort.findById(any()))
                .thenReturn(Optional.of(workspace));
        when(workspaceUserCommandPort.saveWorkspaceUser(any(WorkspaceUser.class)))
                .thenReturn(workspaceUser);
    }


    @DisplayName("워크스페이스 유저 생성 - 성공 / 파일이 없을 경우")
    @Test
    void create_workspace_user_success_null_file() throws Exception {

        // given
        CreateWorkspaceUserRequestDto requestDto = createCreateWorkspaceUserRequestDtoNullFile();
        saveWorkspaceUserProcess();

        // when
        WorkspaceUserResponseDto responseDto = workspaceUserCommandService.createWorkspaceUser(requestDto);

        // then
        assertAll(
                () -> verify(workspaceUserQueryPort).existsByNicknameInWorkspace(requestDto.getWorkspaceId(), requestDto.getNickname()),
                () -> verify(userQueryPort).findById(requestDto.getUserId()),
                () -> verify(workspaceQueryPort).findById(requestDto.getWorkspaceId()),
                () -> verify(fileManager, times(0)).saveProfileImage(any()),
                () -> verify(workspaceUserCommandPort).saveWorkspaceUser(any(WorkspaceUser.class))
        );
    }

    private CreateWorkspaceUserRequestDto createCreateWorkspaceUserRequestDtoNullFile() {
        return CreateWorkspaceUserRequestDto.builder()
                .workspaceId(1L)
                .nickname("테스트닉네임")
                .position("테스트직책")
                .email("test@test.com")
                .phone("010-1234-1234")
                .isAdmin(true)
                .build();
    }
}