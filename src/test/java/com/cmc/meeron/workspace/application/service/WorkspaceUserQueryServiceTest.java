package com.cmc.meeron.workspace.application.service;

import com.cmc.meeron.common.exception.user.NicknameDuplicateException;
import com.cmc.meeron.common.exception.user.WorkspaceUserNotFoundException;
import com.cmc.meeron.user.application.port.in.request.FindWorkspaceUserRequestDtoBuilder;
import com.cmc.meeron.workspace.application.port.out.response.WorkspaceUserQueryResponseDto;
import com.cmc.meeron.user.domain.User;
import com.cmc.meeron.workspace.application.port.in.request.FindWorkspaceUserRequestDto;
import com.cmc.meeron.workspace.application.port.in.response.MyWorkspaceUserResponseDto;
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

import static com.cmc.meeron.user.UserFixture.USER;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class WorkspaceUserQueryServiceTest {

    @Mock WorkspaceUserQueryPort workspaceUserQueryPort;
    @InjectMocks WorkspaceUserQueryService workspaceUserQueryService;

    private User user;

    @BeforeEach
    void setUp() {
        user = USER;
    }

    @DisplayName("회원의 모든 워크스페이스 유저 프로필 가져오기 - 성공")
    @Test
    void get_my_workspace_users_success() throws Exception {

        // given
        List<WorkspaceUser> workspaceUsers = createWorkspaceUsers();
        when(workspaceUserQueryPort.findMyWorkspaceUsers(any()))
                .thenReturn(workspaceUsers);

        // when
        List<MyWorkspaceUserResponseDto> myWorkspaceUsers = workspaceUserQueryService.getMyWorkspaceUsers(user.getId());

        // then
        assertAll(
                () -> verify(workspaceUserQueryPort).findMyWorkspaceUsers(user.getId()),
                () -> assertEquals(workspaceUsers.size(), myWorkspaceUsers.size())
        );
    }

    private List<WorkspaceUser> createWorkspaceUsers() {
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
        when(workspaceUserQueryPort.findWorkspaceUserById(any()))
                .thenReturn(Optional.of(workspaceUser));

        // when
        MyWorkspaceUserResponseDto myWorkspaceUser = workspaceUserQueryService.getMyWorkspaceUser(workspaceUser.getId());

        // then
        assertAll(
                () -> verify(workspaceUserQueryPort).findWorkspaceUserById(workspaceUser.getId()),
                () -> assertEquals(workspaceUser.getId(), myWorkspaceUser.getWorkspaceUserId())
        );
    }

    @DisplayName("회원이 지정한 워크스페이스 유저 가져오기 - 실패 / 해당 유저가 없을 경우")
    @Test
    void get_my_workspace_user_fail_workspace_user_not_found() throws Exception {

        // given
        when(workspaceUserQueryPort.findWorkspaceUserById(any()))
                .thenReturn(Optional.empty());

        // when, then
        assertThrows(WorkspaceUserNotFoundException.class,
                () -> workspaceUserQueryService.getMyWorkspaceUser(1L));
    }

    private WorkspaceUser createWorkspaceUser() {
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
        FindWorkspaceUserRequestDto requestDto = FindWorkspaceUserRequestDtoBuilder.build();
        List<WorkspaceUserQueryResponseDto> workspaceUserQueryResponseDtos = createWorkspaceUserQueryResponseDtos();
        when(workspaceUserQueryPort.findByWorkspaceIdNickname(any(), any()))
                .thenReturn(workspaceUserQueryResponseDtos);

        // when
        List<MyWorkspaceUserResponseDto> responseDtos = workspaceUserQueryService.searchWorkspaceUsers(requestDto);

        // then
        assertAll(
                () -> verify(workspaceUserQueryPort).findByWorkspaceIdNickname(requestDto.getWorkspaceId(), requestDto.getNickname()),
                () -> assertEquals(workspaceUserQueryResponseDtos.size(), responseDtos.size())
        );
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
        when(workspaceUserQueryPort.findByTeamId(any()))
                .thenReturn(workspaceUserQueryResponseDtos);

        // when
        List<MyWorkspaceUserResponseDto> responseDtos = workspaceUserQueryService.getTeamUsers(1L);

        // then
        assertAll(
                () -> assertEquals(workspaceUserQueryResponseDtos.size(), responseDtos.size()),
                () -> verify(workspaceUserQueryPort).findByTeamId(1L)
        );
    }@DisplayName("닉네임 중복 검사 조회 - 성공 / 중복되지 않았을 경우")
    @Test
    void check_duplicate_nickname_success_not_duplicate() throws Exception {

        // given
        FindWorkspaceUserRequestDto requestDto = FindWorkspaceUserRequestDtoBuilder.build();
        when(workspaceUserQueryPort.existsByNicknameInWorkspace(requestDto.getWorkspaceId(),
                requestDto.getNickname()))
                .thenReturn(false);

        // when
        workspaceUserQueryService.checkDuplicateNickname(requestDto);

        // then
        verify(workspaceUserQueryPort).existsByNicknameInWorkspace(requestDto.getWorkspaceId(), requestDto.getNickname());
    }

    @DisplayName("닉네임 중복 검사 조회 - 실패 / 중복되었을 경우")
    @Test
    void check_duplicate_nickname_fail_duplicate() throws Exception {

        // given
        FindWorkspaceUserRequestDto requestDto = FindWorkspaceUserRequestDtoBuilder.build();
        when(workspaceUserQueryPort.existsByNicknameInWorkspace(requestDto.getWorkspaceId(),
                requestDto.getNickname()))
                .thenReturn(true);

        // when, then
        assertThrows(NicknameDuplicateException.class,
                () -> workspaceUserQueryService.checkDuplicateNickname(requestDto));
    }
}