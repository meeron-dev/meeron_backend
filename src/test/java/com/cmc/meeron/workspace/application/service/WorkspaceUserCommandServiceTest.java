package com.cmc.meeron.workspace.application.service;

import com.cmc.meeron.common.exception.team.PreviousBelongToTeamException;
import com.cmc.meeron.common.exception.team.TeamNotFoundException;
import com.cmc.meeron.common.exception.workspace.NicknameDuplicateException;
import com.cmc.meeron.common.exception.user.UserNotFoundException;
import com.cmc.meeron.common.exception.workspace.NotAllFoundWorkspaceUsersException;
import com.cmc.meeron.common.exception.workspace.WorkspaceUserNotFoundException;
import com.cmc.meeron.common.exception.workspace.WorkspaceNotFoundException;
import com.cmc.meeron.file.application.port.in.FileManager;
import com.cmc.meeron.workspace.application.port.in.request.*;
import com.cmc.meeron.team.application.port.out.TeamQueryPort;
import com.cmc.meeron.team.domain.Team;
import com.cmc.meeron.user.application.port.out.UserQueryPort;
import com.cmc.meeron.user.domain.User;
import com.cmc.meeron.workspace.application.port.in.response.WorkspaceUserCommandResponseDto;
import com.cmc.meeron.workspace.application.port.out.WorkspaceQueryPort;
import com.cmc.meeron.workspace.application.port.out.WorkspaceUserCommandPort;
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

import static com.cmc.meeron.file.FileFixture.FILE;
import static com.cmc.meeron.team.TeamFixture.TEAM_1;
import static com.cmc.meeron.user.UserFixture.USER;
import static com.cmc.meeron.workspace.WorkspaceFixture.WORKSPACE_1;
import static com.cmc.meeron.workspace.WorkspaceUserFixture.*;
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
    @InjectMocks WorkspaceUserCommandService workspaceUserCommandService;

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
        WorkspaceUserCommandResponseDto responseDto = workspaceUserCommandService.createWorkspaceUser(requestDto);

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

    @DisplayName("워크스페이스 유저 정보 수정 - 실패 / 워크스페이스 유저가 없을 경우")
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

    @DisplayName("워크스페이스 유저 정보 수정 - 실패 / 닉네임이 중복될 경우")
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
                .nickname("테스트으")
                .phone("01012341234")
                .position("사원")
                .profileImageUrl("https://test.test.com")
                .build();
    }

    @DisplayName("워크스페이스 유저 정보 수정 - 성공 / 프로필 이미지를 줄 경우")
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
        WorkspaceUserCommandResponseDto responseDto = workspaceUserCommandService.modifyWorkspaceUser(requestDto);

        // then
        assertAll(
                () -> verify(workspaceUserQueryPort).findById(requestDto.getWorkspaceUserId()),
                () -> verify(workspaceUserQueryPort).existsByNicknameInWorkspace(workspaceUser.getWorkspace().getId(),
                        requestDto.getNickname()),
                () -> verify(fileManager).saveProfileImage(any()),
                () -> assertEquals(requestDto.getNickname(), responseDto.getNickname()),
                () -> assertEquals(requestDto.getEmail(), responseDto.getContactMail()),
                () -> assertEquals(requestDto.getPhone(), responseDto.getPhone()),
                () -> assertEquals(workspaceUser.getWorkspaceUserInfo().getProfileImageUrl(),
                        responseDto.getProfileImageUrl())
        );
    }

    @DisplayName("워크스페이스 유저 정보 수정 - 성공 / 프로필 이미지를 안줄 경우")
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
        WorkspaceUserCommandResponseDto responseDto = workspaceUserCommandService.modifyWorkspaceUser(requestDto);

        // then
        assertAll(
                () -> verify(workspaceUserQueryPort).findById(requestDto.getWorkspaceUserId()),
                () -> verify(workspaceUserQueryPort).existsByNicknameInWorkspace(workspaceUser.getWorkspace().getId(),
                        requestDto.getNickname()),
                () -> verify(fileManager, times(0)).saveProfileImage(any()),
                () -> assertEquals(requestDto.getNickname(), responseDto.getNickname()),
                () -> assertEquals(requestDto.getEmail(), responseDto.getContactMail()),
                () -> assertEquals(requestDto.getPhone(), responseDto.getPhone()),
                () -> assertEquals(workspaceUser.getWorkspaceUserInfo().getProfileImageUrl(),
                        responseDto.getProfileImageUrl())
        );
    }

    @DisplayName("워크스페이스 유저 생성 - 성공 / 파일이 없을 경우")
    @Test
    void create_workspace_user_success_null_file() throws Exception {

        // given
        CreateWorkspaceUserRequestDto requestDto = createCreateWorkspaceUserRequestDtoNullFile();
        saveWorkspaceUserProcess();

        // when
        WorkspaceUserCommandResponseDto responseDto = workspaceUserCommandService.createWorkspaceUser(requestDto);

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


    @DisplayName("유저가 팀에 들어가기 - 실패 / 팀이 없을 경우")
    @Test
    void join_team_users_fail_not_found_team() throws Exception {

        // given
        JoinTeamUsersRequestDto requestDto = JoinTeamUsersRequestDtoBuilder.build();
        when(teamQueryPort.findById(any()))
                .thenReturn(Optional.empty());

        // when, then
        assertThrows(TeamNotFoundException.class,
                () -> workspaceUserCommandService.joinTeamUsers(requestDto));
    }

    @DisplayName("유저가 팀에 들어가기 - 실패 / 팀에 속한 유저가 있을 경우")
    @Test
    void join_team_users_fail_previous_belong_to_team() throws Exception {

        // given
        JoinTeamUsersRequestDto requestDto = JoinTeamUsersRequestDtoBuilder.build();
        when(teamQueryPort.findById(any()))
                .thenReturn(Optional.of(team));
        when(workspaceUserQueryPort.findAllWorkspaceUsersByIds(any()))
                .thenReturn(List.of(WORKSPACE_USER_1, WORKSPACE_USER_WITH_TEAM));

        // when, then
        assertThrows(PreviousBelongToTeamException.class,
                () -> workspaceUserCommandService.joinTeamUsers(requestDto));
    }

    @DisplayName("유저가 팀에 들어가기 - 실패 / 찾는 유저 수가 다를 경우")
    @Test
    void join_team_users_fail_get_workspace_users() throws Exception {

        // given
        JoinTeamUsersRequestDto requestDto = JoinTeamUsersRequestDtoBuilder.build();
        when(teamQueryPort.findById(any()))
                .thenReturn(Optional.of(team));
        when(workspaceUserQueryPort.findAllWorkspaceUsersByIds(any()))
                .thenReturn(List.of(workspaceUser));

        // when, then
        assertThrows(NotAllFoundWorkspaceUsersException.class,
                () -> workspaceUserCommandService.joinTeamUsers(requestDto));
    }

    @DisplayName("유저가 팀에 들어가기 - 성공")
    @Test
    void join_team_users_success() throws Exception {

        // given
        JoinTeamUsersRequestDto requestDto = JoinTeamUsersRequestDtoBuilder.build();
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

    @DisplayName("팀에서 추방 - 실패 / 워크스페이스 유저가 존재하지 않을 경우")
    @Test
    void exit_team_user_fail_not_found_workspace_user() throws Exception {

        // given
        KickOutTeamUserRequestDto requestDto = KickOutTeamUserRequestDtoBuilder.build();
        when(workspaceUserQueryPort.findById(any()))
                .thenReturn(Optional.empty());

        // when, then
        assertThrows(WorkspaceUserNotFoundException.class,
                () -> workspaceUserCommandService.kickOutTeamUser(requestDto));
    }

    @DisplayName("팀에서 추방 - 성공")
    @Test
    void exit_team_user_success() throws Exception {

        // given
        KickOutTeamUserRequestDto requestDto = KickOutTeamUserRequestDtoBuilder.build();
        when(workspaceUserQueryPort.findById(any()))
                .thenReturn(Optional.of(workspaceUserBelongToTeam));

        // when
        workspaceUserCommandService.kickOutTeamUser(requestDto);

        // then
        assertAll(
                () -> verify(workspaceUserQueryPort).findById(requestDto.getKickOutWorkspaceUserId()),
                () -> assertNull(workspaceUserBelongToTeam.getTeam())
        );
    }
}
