package com.cmc.meeron.workspace.application.service;

import com.cmc.meeron.common.exception.workspace.NicknameDuplicateException;
import com.cmc.meeron.common.exception.workspace.WorkspaceUserNotFoundException;
import com.cmc.meeron.user.application.port.in.request.FindWorkspaceUserRequestDtoBuilder;
import com.cmc.meeron.user.domain.User;
import com.cmc.meeron.workspace.application.port.in.request.FindWorkspaceUserRequestDto;
import com.cmc.meeron.workspace.application.port.in.response.WorkspaceUserQueryResponseDto;
import com.cmc.meeron.workspace.application.port.out.WorkspaceUserQueryPort;
import com.cmc.meeron.workspace.application.port.out.response.WorkspaceUserQuerydslResponseDto;
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
import static com.cmc.meeron.workspace.WorkspaceUserFixture.WORKSPACE_USER_1;
import static com.cmc.meeron.workspace.WorkspaceUserFixture.WORKSPACE_USER_2;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class WorkspaceUserQueryServiceTest {

    @Mock WorkspaceUserQueryPort workspaceUserQueryPort;
    @InjectMocks WorkspaceUserQueryService workspaceUserQueryService;

    private User user;
    private WorkspaceUser noneTeamWorkspaceUser1;
    private WorkspaceUser noneTeamWorkspaceUser2;

    @BeforeEach
    void setUp() {
        user = USER;
        noneTeamWorkspaceUser1 = WORKSPACE_USER_1;
        noneTeamWorkspaceUser2 = WORKSPACE_USER_2;
    }

    @DisplayName("회원의 모든 워크스페이스 유저 프로필 가져오기 - 성공")
    @Test
    void get_my_workspace_users_success() throws Exception {

        // given
        List<WorkspaceUser> workspaceUsers = createWorkspaceUsers();
        when(workspaceUserQueryPort.findMyWorkspaceUsers(any()))
                .thenReturn(workspaceUsers);

        // when
        List<WorkspaceUserQueryResponseDto> myWorkspaceUsers = workspaceUserQueryService.getMyWorkspaceUsers(user.getId());

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
        when(workspaceUserQueryPort.findById(any()))
                .thenReturn(Optional.of(workspaceUser));

        // when
        WorkspaceUserQueryResponseDto myWorkspaceUser = workspaceUserQueryService.getMyWorkspaceUser(workspaceUser.getId());

        // then
        assertAll(
                () -> verify(workspaceUserQueryPort).findById(workspaceUser.getId()),
                () -> assertEquals(workspaceUser.getId(), myWorkspaceUser.getWorkspaceUserId())
        );
    }

    @DisplayName("회원이 지정한 워크스페이스 유저 가져오기 - 실패 / 해당 유저가 없을 경우")
    @Test
    void get_my_workspace_user_fail_workspace_user_not_found() throws Exception {

        // given
        when(workspaceUserQueryPort.findById(any()))
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
        List<WorkspaceUserQuerydslResponseDto> workspaceUserQuerydslResponseDtos = createWorkspaceUserQueryResponseDtos();
        when(workspaceUserQueryPort.findByWorkspaceIdNickname(any(), any()))
                .thenReturn(workspaceUserQuerydslResponseDtos);

        // when
        List<WorkspaceUserQueryResponseDto> responseDtos = workspaceUserQueryService.searchWorkspaceUsers(requestDto);

        // then
        assertAll(
                () -> verify(workspaceUserQueryPort).findByWorkspaceIdNickname(requestDto.getWorkspaceId(), requestDto.getNickname()),
                () -> assertEquals(workspaceUserQuerydslResponseDtos.size(), responseDtos.size())
        );
    }

    private List<WorkspaceUserQuerydslResponseDto> createWorkspaceUserQueryResponseDtos() {
        return List.of(
                WorkspaceUserQuerydslResponseDto.builder()
                        .workspaceId(1L)
                        .workspaceAdmin(false)
                        .workspaceUserId(1L)
                        .nickname("무무")
                        .profileImageUrl(null)
                        .position("사원")
                        .build(),
                WorkspaceUserQuerydslResponseDto.builder()
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
        List<WorkspaceUserQuerydslResponseDto> workspaceUserQuerydslResponseDtos = createWorkspaceUserQueryResponseDtos();
        when(workspaceUserQueryPort.findQueryByTeamId(any()))
                .thenReturn(workspaceUserQuerydslResponseDtos);

        // when
        List<WorkspaceUserQueryResponseDto> responseDtos = workspaceUserQueryService.getTeamUsers(1L);

        // then
        assertAll(
                () -> assertEquals(workspaceUserQuerydslResponseDtos.size(), responseDtos.size()),
                () -> verify(workspaceUserQueryPort).findQueryByTeamId(1L)
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

    @DisplayName("팀에 속하지 않은 워크스페이스 유저 조회 - 성공")
    @Test
    void get_none_team_workspace_users_success() throws Exception {

        // given
        when(workspaceUserQueryPort.findByWorkspaceIdAndTeamIsNull(any()))
                .thenReturn(List.of(noneTeamWorkspaceUser1, noneTeamWorkspaceUser2));

        // when
        List<WorkspaceUserQueryResponseDto> responseDtos = workspaceUserQueryService.getNoneTeamWorkspaceUsers(1L);

        // then
        assertAll(
                () -> verify(workspaceUserQueryPort).findByWorkspaceIdAndTeamIsNull(1L),
                () -> assertEquals(noneTeamWorkspaceUser1.getId(), responseDtos.get(0).getWorkspaceUserId()),
                () -> assertEquals(noneTeamWorkspaceUser2.getId(), responseDtos.get(1).getWorkspaceUserId())
        );
    }
}
