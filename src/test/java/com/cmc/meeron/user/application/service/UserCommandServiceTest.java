package com.cmc.meeron.user.application.service;

import com.cmc.meeron.common.exception.user.UserNotFoundException;
import com.cmc.meeron.common.security.AuthUser;
import com.cmc.meeron.user.application.port.out.UserQueryPort;
import com.cmc.meeron.user.domain.User;
import com.cmc.meeron.workspace.application.port.out.WorkspaceUserQueryPort;
import com.cmc.meeron.workspace.domain.Workspace;
import com.cmc.meeron.workspace.domain.WorkspaceUser;
import com.cmc.meeron.workspace.domain.WorkspaceUserInfo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static com.cmc.meeron.auth.AuthUserFixture.AUTH_USER;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserCommandServiceTest {

    @Mock
    UserQueryPort userQueryPort;
    @Mock
    WorkspaceUserQueryPort workspaceUserQueryPort;
    @InjectMocks
    UserCommandService userCommandService;

    private AuthUser authUser;

    @BeforeEach
    void setUp() {
        authUser = AUTH_USER;
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

    @DisplayName("회원 탈퇴 - 실패 / 이미 탈퇴하거나 없는 회원일 경우")
    @Test
    void withdraw_fail_not_found_user() throws Exception {

        // given
        AuthUser authUser = setUpQuitAuthUser();
        when(userQueryPort.findById(any()))
                .thenReturn(Optional.empty());

        // when
        userCommandService.quit(authUser);

        // then
        assertAll(
                () -> verify(userQueryPort).findById(authUser.getUserId()),
                () -> verify(workspaceUserQueryPort, times(0)).findWithWorkspaceByUserId(authUser.getUserId())
        );
    }

    @DisplayName("회원 탈퇴 - 성공")
    @Test
    void withdraw_success() throws Exception {

        // given
        AuthUser authUser = setUpQuitAuthUser();
        User user = authUser.getUser();
        String email = user.getEmail();
        when(userQueryPort.findById(any()))
                .thenReturn(Optional.of(user));
        List<WorkspaceUser> workspaceUsers = setUpQuitWorkspaceUsersWithWorkspace();
        when(workspaceUserQueryPort.findWithWorkspaceByUserId(any()))
                .thenReturn(workspaceUsers);
        List<WorkspaceUser> workspaceUsersInDeletedWorkspace = setUpWorkspaceUserInDeletedWorkspace();
        when(workspaceUserQueryPort.findByWorkspaceId(any()))
                .thenReturn(workspaceUsersInDeletedWorkspace);

        // when
        userCommandService.quit(authUser);

        // then
        Workspace expectedDeleteWorkspace = workspaceUsers.get(0).getWorkspace();
        WorkspaceUser expectedQuitWorkspaceUser = workspaceUsersInDeletedWorkspace.get(0);
        assertAll(
                () -> verify(workspaceUserQueryPort).findWithWorkspaceByUserId(authUser.getUserId()),
                () -> verify(workspaceUserQueryPort).findByWorkspaceId(expectedDeleteWorkspace.getId()),
                () -> assertTrue(user.isDeleted()),
                () -> assertNotEquals(email, user.getEmail()),
                () -> assertTrue(expectedDeleteWorkspace.isDeleted()),
                () -> assertTrue(expectedQuitWorkspaceUser.isDeleted())
        );
    }

    private AuthUser setUpQuitAuthUser() {
        User user = User.builder()
                .id(1L)
                .email("test@test.com")
                .deleted(false)
                .build();
        return new AuthUser(user);
    }

    private List<WorkspaceUser> setUpQuitWorkspaceUsersWithWorkspace() {
        return List.of(
                WorkspaceUser.builder()
                        .id(1L)
                        .deleted(false)
                        .workspaceUserInfo(WorkspaceUserInfo.builder()
                                .isWorkspaceAdmin(true)
                                .build())
                        .workspace(Workspace.builder()
                                .id(1L)
                                .deleted(false)
                                .build())
                        .build());
    }

    private List<WorkspaceUser> setUpWorkspaceUserInDeletedWorkspace() {
        return List.of(
                WorkspaceUser.builder()
                        .id(3L)
                        .deleted(false)
                        .workspaceUserInfo(WorkspaceUserInfo.builder()
                                .isWorkspaceAdmin(false)
                                .build())
                        .workspace(Workspace.builder()
                                .id(2L)
                                .deleted(false)
                                .build())
                        .build()
        );
    }
}
