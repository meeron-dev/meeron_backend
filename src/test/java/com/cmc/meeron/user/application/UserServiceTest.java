package com.cmc.meeron.user.application;

import com.cmc.meeron.auth.domain.AuthUser;
import com.cmc.meeron.common.exception.user.WorkspaceUserNotFoundException;
import com.cmc.meeron.user.application.dto.response.MeResponseDto;
import com.cmc.meeron.user.application.dto.response.MyWorkspaceUserResponseDto;
import com.cmc.meeron.user.domain.Role;
import com.cmc.meeron.user.domain.User;
import com.cmc.meeron.user.domain.UserProvider;
import com.cmc.meeron.user.domain.WorkspaceUser;
import com.cmc.meeron.user.domain.UserRepository;
import com.cmc.meeron.workspace.domain.Workspace;
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
class UserServiceTest {

    @Mock UserRepository userRepository;
    @InjectMocks UserService userService;

    @DisplayName("회원 정보 가져오기 - 성공")
    @Test
    void get_me_success() throws Exception {

        // given
        User user = createUser();
        AuthUser authUser = AuthUser.of(user);

        // when
        MeResponseDto meResponseDto = userService.getMe(authUser);

        // then
        assertAll(
                () -> assertEquals(user.getId(), meResponseDto.getUserId()),
                () -> assertEquals(user.getEmail(), meResponseDto.getLoginEmail()),
                () -> assertEquals(user.getContactEmail(), meResponseDto.getContactEmail()),
                () -> assertEquals(user.getName(), meResponseDto.getName()),
                () -> assertEquals(user.getProfileImageUrl(), meResponseDto.getProfileImageUrl()),
                () -> assertEquals(user.getPhone(), meResponseDto.getPhone())
        );
    }

    private User createUser() {
        return User.builder()
                .id(1L)
                .email("test@gmail.com")
                .role(Role.USER)
                .userProvider(UserProvider.KAKAO)
                .name("테스트")
                .nickname("테스트닉네임")
                .profileImageUrl(null)
                .contactEmail("test@naver.com")
                .build();
    }

    @DisplayName("회원의 모든 워크스페이스 유저 프로필 가져오기 - 성공")
    @Test
    void get_my_workspace_users_success() throws Exception {

        // given
        User user = createUser();
        List<WorkspaceUser> workspaceUsers = createWorkspaceUsers();
        when(userRepository.findMyWorkspaceUsers(any()))
                .thenReturn(workspaceUsers);

        // when
        List<MyWorkspaceUserResponseDto> myWorkspaceUsers = userService.getMyWorkspaceUsers(user.getId());

        // then
        assertAll(
                () -> verify(userRepository).findMyWorkspaceUsers(user.getId()),
                () -> assertEquals(workspaceUsers.size(), myWorkspaceUsers.size())
        );
    }

    private List<WorkspaceUser> createWorkspaceUsers() {
        User user = createUser();
        return List.of(
                WorkspaceUser.builder()
                        .id(1L)
                        .user(user)
                        .workspace(Workspace.builder().id(1L).build())
                        .nickname("테스트닉네임1")
                        .workspaceAdmin(false)
                        .position("과장")
                        .build(),
                WorkspaceUser.builder()
                        .id(2L)
                        .user(user)
                        .workspace(Workspace.builder().id(2L).build())
                        .nickname("테스트닉네임2")
                        .workspaceAdmin(true)
                        .position("매니저")
                        .build()
        );
    }

    @DisplayName("회원이 지정한 워크스페이스 유저 가져오기 - 성공")
    @Test
    void get_my_workspace_user_success() throws Exception {

        // given
        WorkspaceUser workspaceUser = createWorkspaceUser();
        when(userRepository.findWorkspaceUserById(any()))
                .thenReturn(Optional.of(workspaceUser));

        // when
        MyWorkspaceUserResponseDto myWorkspaceUser = userService.getMyWorkspaceUser(workspaceUser.getId());

        // then
        assertAll(
                () -> verify(userRepository).findWorkspaceUserById(workspaceUser.getId()),
                () -> assertEquals(workspaceUser.getId(), myWorkspaceUser.getWorkspaceUserId())
        );
    }

    @DisplayName("회원이 지정한 워크스페이스 유저 가져오기 - 실패 / 해당 유저가 없을 경우")
    @Test
    void get_my_workspace_user_fail_workspace_user_not_found() throws Exception {

        // given
        when(userRepository.findWorkspaceUserById(any()))
                .thenReturn(Optional.empty());

        // when, then
        assertThrows(WorkspaceUserNotFoundException.class,
                () -> userService.getMyWorkspaceUser(1L));
    }

    private WorkspaceUser createWorkspaceUser() {
        User user = createUser();
        return WorkspaceUser.builder()
                .id(1L)
                .user(user)
                .workspace(Workspace.builder().id(1L).build())
                .nickname("테스트닉네임1")
                .workspaceAdmin(false)
                .position("과장")
                .build();
    }
}
