package com.cmc.meeron.workspaceuser.application.service;

import com.cmc.meeron.common.exception.team.PreviousBelongToTeamException;
import com.cmc.meeron.common.exception.team.TeamNotFoundException;
import com.cmc.meeron.common.exception.user.UserNotFoundException;
import com.cmc.meeron.common.exception.workspace.NicknameDuplicateException;
import com.cmc.meeron.common.exception.workspace.NotAllFoundWorkspaceUsersException;
import com.cmc.meeron.common.exception.workspace.WorkspaceNotFoundException;
import com.cmc.meeron.common.exception.workspace.WorkspaceUserNotFoundException;
import com.cmc.meeron.file.application.port.in.FileManager;
import com.cmc.meeron.team.application.port.in.request.JoinTeamMembersRequestDto;
import com.cmc.meeron.team.application.port.in.request.JoinTeamMembersRequestDtoBuilder;
import com.cmc.meeron.team.application.port.in.request.EjectTeamMemberRequestDto;
import com.cmc.meeron.team.application.port.in.request.EjectTeamMemberRequestDtoBuilder;
import com.cmc.meeron.team.application.port.out.TeamQueryPort;
import com.cmc.meeron.team.domain.Team;
import com.cmc.meeron.user.application.port.out.UserQueryPort;
import com.cmc.meeron.user.domain.User;
import com.cmc.meeron.workspace.application.port.out.WorkspaceQueryPort;
import com.cmc.meeron.workspace.domain.Workspace;
import com.cmc.meeron.workspaceuser.application.port.in.request.*;
import com.cmc.meeron.workspaceuser.application.port.in.response.WorkspaceUserResponseDto;
import com.cmc.meeron.workspaceuser.application.port.out.WorkspaceUserCommandPort;
import com.cmc.meeron.workspaceuser.application.port.out.WorkspaceUserQueryPort;
import com.cmc.meeron.workspaceuser.domain.WorkspaceUser;
import com.cmc.meeron.workspaceuser.domain.WorkspaceUserInfo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static com.cmc.meeron.file.FileFixture.FILE;
import static com.cmc.meeron.team.TeamFixture.TEAM_1;
import static com.cmc.meeron.user.UserFixture.USER;
import static com.cmc.meeron.workspace.WorkspaceFixture.WORKSPACE_1;
import static com.cmc.meeron.workspaceuser.WorkspaceUserFixture.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WorkspaceUserCommandServiceTest {

    @Mock UserQueryPort userQueryPort;
    @Mock WorkspaceQueryPort workspaceQueryPort;
    @Mock WorkspaceUserQueryPort workspaceUserQueryPort;
    @Mock WorkspaceUserCommandPort workspaceUserCommandPort;
    @Mock FileManager fileManager;
    @Mock TeamQueryPort teamQueryPort;
    @InjectMocks
    WorkspaceUserCommandService workspaceUserCommandService;

    private Workspace workspace;
    private User user;
    private Team team;
    private WorkspaceUser workspaceUser;
    private WorkspaceUser workspaceUser2;
    private WorkspaceUser workspaceUserBelongToTeam;

    @BeforeEach
    void setUp() {
        user = USER;
        team = TEAM_1;
        workspace = WORKSPACE_1;
        workspaceUser = WORKSPACE_USER_FOR_JOIN_TEAM2;
        workspaceUser2 = WORKSPACE_USER_2;
        workspaceUserBelongToTeam = WORKSPACE_USER_WITH_TEAM2;
    }

    @DisplayName("?????????????????? ?????? ?????? - ?????? / ???????????? ??????????????? ??????")
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
                .nickname("??????????????????")
                .position("???????????????")
                .email("test@test.com")
                .phone("010-1234-1234")
                .isAdmin(true)
                .build();
    }

    @DisplayName("?????????????????? ?????? ?????? - ?????? / ???????????? ?????? ????????????????????? ??????")
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

    @DisplayName("?????????????????? ?????? ?????? - ?????? / ???????????? ?????? ????????? ??????")
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

    @DisplayName("?????????????????? ?????? ?????? - ??????")
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
                () -> verify(workspaceUserCommandPort).saveWorkspaceUser(any(WorkspaceUser.class)),
                () -> assertThat(responseDto)
                        .usingRecursiveComparison()
                        .isEqualTo(WorkspaceUserResponseDto.from(workspaceUser))
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

    @DisplayName("?????????????????? ?????? ?????? ?????? - ?????? / ?????????????????? ????????? ?????? ??????")
    @Test
    void modify_workspace_user_fail_not_found_workspace_user() throws Exception {

        // given
        ModifyWorkspaceUserRequestDto requestDto = ModifyWorkspaceUserRequestDtoBuilder.build();
        when(workspaceUserQueryPort.findById(any()))
                .thenReturn(Optional.empty());

        // when, then
        assertThrows(WorkspaceUserNotFoundException.class,
                () -> workspaceUserCommandService.modifyWorkspaceUser(requestDto));
    }

    @DisplayName("?????????????????? ?????? ?????? ?????? - ?????? / ???????????? ????????? ??????")
    @Test
    void modify_workspace_user_fail_duplicate_nickname() throws Exception {

        // given
        ModifyWorkspaceUserRequestDto requestDto = ModifyWorkspaceUserRequestDtoBuilder.build();
        WorkspaceUser workspaceUser = createWorkspaceUser();
        when(workspaceUserQueryPort.findById(any()))
                .thenReturn(Optional.of(workspaceUser));
        when(workspaceUserQueryPort.existsByNicknameInWorkspace(any(), any()))
                .thenReturn(true);

        // when, then
        assertThrows(NicknameDuplicateException.class,
                () -> workspaceUserCommandService.modifyWorkspaceUser(requestDto));
    }

    private WorkspaceUser createWorkspaceUser() {
        return WorkspaceUser.builder()
                .id(59L)
                .workspace(WORKSPACE_1)
                .workspaceUserInfo(createWorkspaceUserInfo())
                .user(USER)
                .build();
    }

    private WorkspaceUserInfo createWorkspaceUserInfo() {
        return WorkspaceUserInfo.builder()
                .contactMail("beforemodify@test.com")
                .nickname("????????????")
                .phone("01012341234")
                .position("??????")
                .profileImageUrl("https://test.test.com")
                .build();
    }

    @DisplayName("?????????????????? ?????? ?????? ?????? - ?????? / ????????? ???????????? ??? ??????")
    @Test
    void modify_workspace_user_success_exists_file() throws Exception {

        // given
        ModifyWorkspaceUserRequestDto requestDto = ModifyWorkspaceUserRequestDtoBuilder.build();
        WorkspaceUser workspaceUser = createWorkspaceUser();
        when(workspaceUserQueryPort.findById(any()))
                .thenReturn(Optional.of(workspaceUser));
        when(workspaceUserQueryPort.existsByNicknameInWorkspace(any(), any()))
                .thenReturn(false);

        // when
        WorkspaceUserResponseDto responseDto = workspaceUserCommandService.modifyWorkspaceUser(requestDto);

        // then
        assertAll(
                () -> verify(workspaceUserQueryPort).findById(requestDto.getWorkspaceUserId()),
                () -> verify(workspaceUserQueryPort).existsByNicknameInWorkspace(workspaceUser.getWorkspace().getId(),
                        requestDto.getNickname()),
                () -> verify(fileManager).saveProfileImage(any()),
                () -> assertThat(responseDto)
                        .usingRecursiveComparison()
                        .isEqualTo(WorkspaceUserResponseDto.from(workspaceUser))
        );
    }

    @DisplayName("?????????????????? ?????? ?????? ?????? - ?????? / ????????? ???????????? ?????? ??????")
    @Test
    void modify_workspace_user_success_not_exists_file() throws Exception {

        // given
        ModifyWorkspaceUserRequestDto requestDto = ModifyWorkspaceUserRequestDtoBuilder.buildNotExistFile();
        WorkspaceUser workspaceUser = createWorkspaceUser();
        when(workspaceUserQueryPort.findById(any()))
                .thenReturn(Optional.of(workspaceUser));
        when(workspaceUserQueryPort.existsByNicknameInWorkspace(any(), any()))
                .thenReturn(false);

        // when
        WorkspaceUserResponseDto responseDto = workspaceUserCommandService.modifyWorkspaceUser(requestDto);

        // then
        assertAll(
                () -> verify(workspaceUserQueryPort).findById(requestDto.getWorkspaceUserId()),
                () -> verify(workspaceUserQueryPort).existsByNicknameInWorkspace(workspaceUser.getWorkspace().getId(),
                        requestDto.getNickname()),
                () -> verify(fileManager, times(0)).saveProfileImage(any()),
                () -> assertThat(responseDto)
                        .usingRecursiveComparison()
                        .isEqualTo(WorkspaceUserResponseDto.from(workspaceUser))
        );
    }

    @DisplayName("?????????????????? ?????? ?????? - ?????? / ????????? ?????? ??????")
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
                () -> verify(workspaceUserCommandPort).saveWorkspaceUser(any(WorkspaceUser.class)),
                () -> assertThat(responseDto)
                        .usingRecursiveComparison()
                        .isEqualTo(WorkspaceUserResponseDto.from(workspaceUser))
        );
    }

    private CreateWorkspaceUserRequestDto createCreateWorkspaceUserRequestDtoNullFile() {
        return CreateWorkspaceUserRequestDto.builder()
                .workspaceId(1L)
                .nickname("??????????????????")
                .position("???????????????")
                .email("test@test.com")
                .phone("010-1234-1234")
                .isAdmin(true)
                .build();
    }


    @DisplayName("????????? ?????? ???????????? - ?????? / ?????? ?????? ??????")
    @Test
    void join_team_users_fail_not_found_team() throws Exception {

        // given
        JoinTeamMembersRequestDto requestDto = JoinTeamMembersRequestDtoBuilder.build();
        when(teamQueryPort.findById(any()))
                .thenReturn(Optional.empty());

        // when, then
        assertThrows(TeamNotFoundException.class,
                () -> workspaceUserCommandService.joinTeamUsers(requestDto));
    }

    @DisplayName("????????? ?????? ???????????? - ?????? / ?????? ?????? ????????? ?????? ??????")
    @Test
    void join_team_users_fail_previous_belong_to_team() throws Exception {

        // given
        JoinTeamMembersRequestDto requestDto = JoinTeamMembersRequestDtoBuilder.build();
        when(teamQueryPort.findById(any()))
                .thenReturn(Optional.of(team));
        when(workspaceUserQueryPort.findAllWorkspaceUsersByIds(any()))
                .thenReturn(List.of(WORKSPACE_USER_1, WORKSPACE_USER_WITH_TEAM));

        // when, then
        assertThrows(PreviousBelongToTeamException.class,
                () -> workspaceUserCommandService.joinTeamUsers(requestDto));
    }

    @DisplayName("????????? ?????? ???????????? - ?????? / ?????? ?????? ?????? ?????? ??????")
    @Test
    void join_team_users_fail_get_workspace_users() throws Exception {

        // given
        JoinTeamMembersRequestDto requestDto = JoinTeamMembersRequestDtoBuilder.build();
        when(teamQueryPort.findById(any()))
                .thenReturn(Optional.of(team));
        when(workspaceUserQueryPort.findAllWorkspaceUsersByIds(any()))
                .thenReturn(List.of(workspaceUser));

        // when, then
        assertThrows(NotAllFoundWorkspaceUsersException.class,
                () -> workspaceUserCommandService.joinTeamUsers(requestDto));
    }

    @DisplayName("????????? ?????? ???????????? - ??????")
    @Test
    void join_team_users_success() throws Exception {

        // given
        JoinTeamMembersRequestDto requestDto = JoinTeamMembersRequestDtoBuilder.build();
        when(teamQueryPort.findById(any()))
                .thenReturn(Optional.of(team));
        when(workspaceUserQueryPort.findAllWorkspaceUsersByIds(any()))
                .thenReturn(List.of(workspaceUser, workspaceUser2));

        // when
        workspaceUserCommandService.joinTeamUsers(requestDto);

        // then
        assertAll(
                () -> verify(teamQueryPort).findById(requestDto.getTeamId()),
                () -> verify(workspaceUserQueryPort).findAllWorkspaceUsersByIds(requestDto.getWorkspaceUserIds()),
                () -> assertNotNull(workspaceUser.getTeam()),
                () -> assertNotNull(workspaceUser2.getTeam())
        );
    }

    @DisplayName("????????? ?????? - ?????? / ?????????????????? ????????? ???????????? ?????? ??????")
    @Test
    void exit_team_user_fail_not_found_workspace_user() throws Exception {

        // given
        EjectTeamMemberRequestDto requestDto = EjectTeamMemberRequestDtoBuilder.build();
        when(workspaceUserQueryPort.findById(any()))
                .thenReturn(Optional.empty());

        // when, then
        assertThrows(WorkspaceUserNotFoundException.class,
                () -> workspaceUserCommandService.kickOutTeamUser(requestDto));
    }

    @DisplayName("????????? ?????? - ??????")
    @Test
    void exit_team_user_success() throws Exception {

        // given
        EjectTeamMemberRequestDto requestDto = EjectTeamMemberRequestDtoBuilder.build();
        when(workspaceUserQueryPort.findById(any()))
                .thenReturn(Optional.of(workspaceUserBelongToTeam));

        // when
        workspaceUserCommandService.kickOutTeamUser(requestDto);

        // then
        assertAll(
                () -> verify(workspaceUserQueryPort).findById(requestDto.getEjectWorkspaceUserId()),
                () -> assertNull(workspaceUserBelongToTeam.getTeam())
        );
    }
}
