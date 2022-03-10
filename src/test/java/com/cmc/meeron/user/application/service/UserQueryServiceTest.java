package com.cmc.meeron.user.application.service;

import com.cmc.meeron.common.security.AuthUser;
import com.cmc.meeron.common.exception.user.WorkspaceUserNotFoundException;
import com.cmc.meeron.user.application.port.in.request.FindWorkspaceUserRequestDto;
import com.cmc.meeron.user.application.port.in.response.MeResponseDto;
import com.cmc.meeron.user.application.port.in.response.MyWorkspaceUserResponseDto;
import com.cmc.meeron.user.application.port.in.response.WorkspaceUserResponseDto;
import com.cmc.meeron.user.application.port.out.UserQueryPort;
import com.cmc.meeron.user.domain.*;
import com.cmc.meeron.user.application.port.out.response.WorkspaceUserQueryResponseDto;
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
class UserQueryServiceTest {

    @Mock
    UserQueryPort userQueryPort;
    @InjectMocks
    UserQueryService userQueryService;

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
                        .nickname("테스트닉네임1")
                        .isWorkspaceAdmin(false)
                        .position("과장")
                        .build(),
                WorkspaceUser.builder()
                        .id(2L)
                        .user(user)
                        .workspace(Workspace.builder().id(2L).build())
                        .nickname("테스트닉네임2")
                        .isWorkspaceAdmin(true)
                        .position("매니저")
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
                .nickname("테스트닉네임1")
                .isWorkspaceAdmin(false)
                .position("과장")
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
        List<WorkspaceUserResponseDto> responseDtos = userQueryService.searchWorkspaceUsers(requestDto);

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
                        .workspaceUserId(1L)
                        .nickname("무무")
                        .profileImageUrl(null)
                        .position("사원")
                        .build(),
                WorkspaceUserQueryResponseDto.builder()
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
        List<WorkspaceUserResponseDto> workspaceUserResponseDtos = userQueryService.getTeamUsers(1L);

        // then
        assertAll(
                () -> assertEquals(workspaceUserQueryResponseDtos.size(), workspaceUserResponseDtos.size()),
                () -> verify(userQueryPort).findByTeamId(1L)
        );
    }
}
