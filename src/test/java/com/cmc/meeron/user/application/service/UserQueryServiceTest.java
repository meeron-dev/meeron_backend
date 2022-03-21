package com.cmc.meeron.user.application.service;

import com.cmc.meeron.common.exception.user.UserNotFoundException;
import com.cmc.meeron.common.exception.user.WorkspaceUserNotFoundException;
import com.cmc.meeron.common.security.AuthUser;
import com.cmc.meeron.user.application.port.in.request.FindWorkspaceUserRequestDto;
import com.cmc.meeron.user.application.port.in.response.MeResponseDto;
import com.cmc.meeron.user.application.port.in.response.MyWorkspaceUserResponseDto;
import com.cmc.meeron.user.application.port.out.UserQueryPort;
import com.cmc.meeron.user.application.port.out.response.WorkspaceUserQueryResponseDto;
import com.cmc.meeron.user.domain.*;
import com.cmc.meeron.workspace.domain.Workspace;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static com.cmc.meeron.user.UserFixture.NOT_NAMED_USER;
import static com.cmc.meeron.user.UserFixture.USER;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserQueryServiceTest {

    @Mock
    UserQueryPort userQueryPort;
    @InjectMocks
    UserQueryService userQueryService;

    private User user;
    private User notNamedUser;

    @BeforeEach
    void setUp() {
        user = USER;
        notNamedUser = NOT_NAMED_USER;
    }

    @DisplayName("회원 정보 가져오기 - 성공")
    @Test
    void get_me_success() throws Exception {

        // given
        User user = createUser();
        AuthUser authUser = AuthUser.of(user);

        // when
        MeResponseDto meResponseDto = userQueryService.getMe(authUser);

        // then
        assertAll(
                () -> assertEquals(user.getId(), meResponseDto.getUserId()),
                () -> assertEquals(user.getEmail(), meResponseDto.getLoginEmail()),
                () -> assertEquals(user.getName(), meResponseDto.getName()),
                () -> assertEquals(user.getProfileImageUrl(), meResponseDto.getProfileImageUrl())
        );
    }

    private User createUser() {
        return User.builder()
                .id(1L)
                .email("test@gmail.com")
                .role(Role.USER)
                .userProvider(UserProvider.KAKAO)
                .name("테스트")
                .profileImageUrl(null)
                .build();
    }

    @DisplayName("회원의 모든 워크스페이스 유저 프로필 가져오기 - 성공")
    @Test
    void get_my_workspace_users_success() throws Exception {

        // given
        User user = createUser();
        List<WorkspaceUser> workspaceUsers = createWorkspaceUsers();
        when(userQueryPort.findMyWorkspaceUsers(any()))
                .thenReturn(workspaceUsers);

        // when
        List<MyWorkspaceUserResponseDto> myWorkspaceUsers = userQueryService.getMyWorkspaceUsers(user.getId());

        // then
        assertAll(
                () -> verify(userQueryPort).findMyWorkspaceUsers(user.getId()),
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
                        .workspaceUserInfo(
                                WorkspaceUserInfo.builder()
                                        .nickname("테스트닉네임1")
                                        .isWorkspaceAdmin(false)
                                        .position("과장")
                                        .profileImageUrl("https://test.com/123")
                                        .build())
                        .build(),
                WorkspaceUser.builder()
                        .id(2L)
                        .user(user)
                        .workspace(Workspace.builder().id(2L).build())
                        .workspaceUserInfo(
                                WorkspaceUserInfo.builder()
                                        .nickname("테스트닉네임2")
                                        .isWorkspaceAdmin(true)
                                        .position("매니저")
                                        .profileImageUrl("https://test.com/123")
                                        .build())
                        .build()
        );
    }

    @DisplayName("회원이 지정한 워크스페이스 유저 가져오기 - 성공")
    @Test
    void get_my_workspace_user_success() throws Exception {

        // given
        WorkspaceUser workspaceUser = createWorkspaceUser();
        when(userQueryPort.findWorkspaceUserById(any()))
                .thenReturn(Optional.of(workspaceUser));

        // when
        MyWorkspaceUserResponseDto myWorkspaceUser = userQueryService.getMyWorkspaceUser(workspaceUser.getId());

        // then
        assertAll(
                () -> verify(userQueryPort).findWorkspaceUserById(workspaceUser.getId()),
                () -> assertEquals(workspaceUser.getId(), myWorkspaceUser.getWorkspaceUserId())
        );
    }

    @DisplayName("회원이 지정한 워크스페이스 유저 가져오기 - 실패 / 해당 유저가 없을 경우")
    @Test
    void get_my_workspace_user_fail_workspace_user_not_found() throws Exception {

        // given
        when(userQueryPort.findWorkspaceUserById(any()))
                .thenReturn(Optional.empty());

        // when, then
        assertThrows(WorkspaceUserNotFoundException.class,
                () -> userQueryService.getMyWorkspaceUser(1L));
    }

    private WorkspaceUser createWorkspaceUser() {
        User user = createUser();
        return WorkspaceUser.builder()
                .id(1L)
                .user(user)
                .workspace(Workspace.builder().id(1L).build())
                .workspaceUserInfo(
                        WorkspaceUserInfo.builder()
                                .profileImageUrl("https://test.com/123")
                                .nickname("테스트닉네임1")
                                .isWorkspaceAdmin(false)
                                .position("과장")
                                .build())
                .build();
    }

    @DisplayName("워크스페이스 유저 닉네임, 워크스페이스 id로 조회 - 성공")
    @Test
    void search_workspace_users_success() throws Exception {

        // given
        FindWorkspaceUserRequestDto requestDto = createFindWorkspaceUserRequestDto();
        List<WorkspaceUserQueryResponseDto> workspaceUserQueryResponseDtos = createWorkspaceUserQueryResponseDtos();
        when(userQueryPort.findByWorkspaceIdNickname(any(), any()))
                .thenReturn(workspaceUserQueryResponseDtos);

        // when
        List<MyWorkspaceUserResponseDto> responseDtos = userQueryService.searchWorkspaceUsers(requestDto);

        // then
        assertAll(
                () -> verify(userQueryPort).findByWorkspaceIdNickname(1L, "무"),
                () -> assertEquals(workspaceUserQueryResponseDtos.size(), responseDtos.size())
        );
    }

    private FindWorkspaceUserRequestDto createFindWorkspaceUserRequestDto() {
        return FindWorkspaceUserRequestDto.builder()
                .workspaceId(1L)
                .nickname("무")
                .build();
    }

    private List<WorkspaceUserQueryResponseDto> createWorkspaceUserQueryResponseDtos() {
        return List.of(
                WorkspaceUserQueryResponseDto.builder()
                        .workspaceId(1L)
                        .workspaceAdmin(false)
                        .workspaceUserId(1L)
                        .nickname("무무")
                        .profileImageUrl(null)
                        .position("사원")
                        .build(),
                WorkspaceUserQueryResponseDto.builder()
                        .workspaceId(1L)
                        .workspaceAdmin(true)
                        .workspaceUserId(2L)
                        .nickname("무무무")
                        .profileImageUrl("https://image.com/123")
                        .position("사원")
                        .build()
        );
    }

    @DisplayName("팀원 정보 조회 - 성공")
    @Test
    void get_team_users_success() throws Exception {

        // given
        List<WorkspaceUserQueryResponseDto> workspaceUserQueryResponseDtos = createWorkspaceUserQueryResponseDtos();
        when(userQueryPort.findByTeamId(any()))
                .thenReturn(workspaceUserQueryResponseDtos);

        // when
        List<MyWorkspaceUserResponseDto> responseDtos = userQueryService.getTeamUsers(1L);

        // then
        assertAll(
                () -> assertEquals(workspaceUserQueryResponseDtos.size(), responseDtos.size()),
                () -> verify(userQueryPort).findByTeamId(1L)
        );
    }

    @DisplayName("사용자가 이름을 입력했는지 체크 - 성공 / 입력했을경우")
    @Test
    void check_named_user_success_named() throws Exception {

        // given
        when(userQueryPort.findById(any()))
                .thenReturn(Optional.of(user));

        // when
        boolean result = userQueryService.checkNamedUser(1L);

        // then
        assertAll(
                () -> verify(userQueryPort).findById(1L),
                () -> assertTrue(result)
        );
    }

    @DisplayName("사용자가 이름을 입력했는지 체크 - 성공 / 입력하지 않았을경우")
    @Test
    void check_named_user_success_not_named() throws Exception {

        // given
        when(userQueryPort.findById(any()))
                .thenReturn(Optional.of(notNamedUser));

        // when
        boolean result = userQueryService.checkNamedUser(1L);

        // then
        assertAll(
                () -> verify(userQueryPort).findById(1L),
                () -> assertFalse(result)
        );
    }

    @DisplayName("사용자가 이름을 입력했는지 체크 - 실패 / 유저가 존재하지 않을 경우")
    @Test
    void check_entered_user_name_fail_not_found_user() throws Exception {

        // given
        when(userQueryPort.findById(any()))
                .thenReturn(Optional.empty());

        // when, then
        assertThrows(UserNotFoundException.class,
                () -> userQueryService.checkNamedUser(1L));
    }
}
