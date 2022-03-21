package com.cmc.meeron.user.application.service;

import com.cmc.meeron.common.exception.user.NicknameDuplicateException;
import com.cmc.meeron.common.exception.user.UserNotFoundException;
import com.cmc.meeron.common.exception.workspace.WorkspaceNotFoundException;
import com.cmc.meeron.common.security.AuthUser;
import com.cmc.meeron.file.application.port.in.FileManager;
import com.cmc.meeron.user.application.port.in.request.CreateWorkspaceUserRequestDto;
import com.cmc.meeron.user.application.port.in.response.WorkspaceUserResponseDto;
import com.cmc.meeron.user.application.port.out.UserCommandPort;
import com.cmc.meeron.user.application.port.out.UserQueryPort;
import com.cmc.meeron.user.domain.User;
import com.cmc.meeron.user.domain.WorkspaceUser;
import com.cmc.meeron.workspace.application.port.out.WorkspaceQueryPort;
import com.cmc.meeron.workspace.domain.Workspace;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static com.cmc.meeron.auth.AuthUserFixture.AUTH_USER;
import static com.cmc.meeron.file.FileFixture.FILE;
import static com.cmc.meeron.user.WorkspaceUserFixture.WORKSPACE_USER_1;
import static com.cmc.meeron.workspace.WorkspaceFixture.WORKSPACE_1;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserCommandServiceTest {

    @Mock UserQueryPort userQueryPort;
    @Mock UserCommandPort userCommandPort;
    @Mock WorkspaceQueryPort workspaceQueryPort;
    @Mock FileManager fileManager;
    @InjectMocks UserCommandService userCommandService;

    private AuthUser authUser;
    private Workspace workspace;
    private User user;
    private WorkspaceUser workspaceUser;

    @BeforeEach
    void setUp() {
        authUser = AUTH_USER;
        user = authUser.getUser();
        workspace = WORKSPACE_1;
        workspaceUser = WORKSPACE_USER_1;
    }

    @DisplayName("유저 성함 저장 - 성공")
    @Test
    void set_name_success() throws Exception {

        // given
        String name = "테스트";
        when(userQueryPort.findById(any()))
                .thenReturn(Optional.of(authUser.getUser()));

        // when
        userCommandService.setName(authUser, name);

        // then
        User user = authUser.getUser();
        assertAll(
                () -> verify(userQueryPort).findById(authUser.getUserId()),
                () -> assertEquals(name, user.getName())
        );
    }

    @DisplayName("유저 성함 저장 - 실패 / 존재하지 않는 유저의 경우")
    @Test
    void set_name_fail_not_found_user() throws Exception {

        // given
        String name = "테스트";
        when(userQueryPort.findById(any())).thenReturn(Optional.empty());

        // when, then
        assertThrows(UserNotFoundException.class,
                () -> userCommandService.setName(authUser, name));
    }

    @DisplayName("워크스페이스 유저 생성 - 실패 / 닉네임이 중복되었을 경우")
    @Test
    void create_workspace_user_fail_valid_nickname() throws Exception {

        // given
        CreateWorkspaceUserRequestDto requestDto = createCreateWorkspaceUserRequestDto();
        when(userQueryPort.existsByNicknameInWorkspace(any(), any()))
                .thenReturn(true);

        // when, then
        assertThrows(NicknameDuplicateException.class,
                () -> userCommandService.createWorkspaceUser(requestDto));
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
                () -> userCommandService.createWorkspaceUser(requestDto));
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
                () -> userCommandService.createWorkspaceUser(requestDto));
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
        WorkspaceUserResponseDto responseDto = userCommandService.createWorkspaceUser(requestDto);

        // then
        assertAll(
                () -> verify(userQueryPort).existsByNicknameInWorkspace(requestDto.getWorkspaceId(), requestDto.getNickname()),
                () -> verify(userQueryPort).findById(requestDto.getUserId()),
                () -> verify(workspaceQueryPort).findById(requestDto.getWorkspaceId()),
                () -> verify(fileManager).saveProfileImage(any()),
                () -> verify(userCommandPort).saveWorkspaceUser(any(WorkspaceUser.class))
        );
    }

    private void saveWorkspaceUserProcess() {
        when(userQueryPort.existsByNicknameInWorkspace(any(), any()))
                .thenReturn(false);
        when(userQueryPort.findById(any()))
                .thenReturn(Optional.of(user));
        when(workspaceQueryPort.findById(any()))
                .thenReturn(Optional.of(workspace));
        when(userCommandPort.saveWorkspaceUser(any(WorkspaceUser.class)))
                .thenReturn(workspaceUser);
    }


    @DisplayName("워크스페이스 유저 생성 - 성공 / 파일이 없을 경우")
    @Test
    void create_workspace_user_success_null_file() throws Exception {

        // given
        CreateWorkspaceUserRequestDto requestDto = createCreateWorkspaceUserRequestDtoNullFile();
        saveWorkspaceUserProcess();

        // when
        WorkspaceUserResponseDto responseDto = userCommandService.createWorkspaceUser(requestDto);

        // then
        assertAll(
                () -> verify(userQueryPort).existsByNicknameInWorkspace(requestDto.getWorkspaceId(), requestDto.getNickname()),
                () -> verify(userQueryPort).findById(requestDto.getUserId()),
                () -> verify(workspaceQueryPort).findById(requestDto.getWorkspaceId()),
                () -> verify(fileManager, times(0)).saveProfileImage(any()),
                () -> verify(userCommandPort).saveWorkspaceUser(any(WorkspaceUser.class))
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
