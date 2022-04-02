package com.cmc.meeron.workspace.domain;

import com.cmc.meeron.common.exception.team.PreviousBelongToTeamException;
import com.cmc.meeron.common.exception.workspace.NotAdminException;
import com.cmc.meeron.common.exception.workspace.WorkspaceUsersNotInEqualWorkspaceException;
import com.cmc.meeron.team.domain.Team;
import com.cmc.meeron.user.domain.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.cmc.meeron.team.TeamFixture.TEAM_1;
import static com.cmc.meeron.user.UserFixture.USER;
import static com.cmc.meeron.workspace.WorkspaceFixture.WORKSPACE_1;
import static com.cmc.meeron.workspace.WorkspaceUserFixture.*;
import static com.cmc.meeron.workspace.WorkspaceUserInfoFixture.WORKSPACE_USER_INFO;
import static org.junit.jupiter.api.Assertions.*;

public class WorkspaceUserTest {

    private Workspace workspace;
    private List<WorkspaceUser> workspaceUsers;
    private User user;
    private WorkspaceUser workspaceUser;
    private WorkspaceUser workspaceUserWithTeam;
    private Team team;
    private WorkspaceUser admin;

    @DisplayName("하나의 워크스페이스 내에 있는지 검증 - 성공")
    @Test
    void valid_in_workspace_success() throws Exception {

        // given
        workspace = WORKSPACE_1;
        workspaceUsers = new ArrayList<>(Arrays.asList(WORKSPACE_USER_1));

        // when, then
        assertDoesNotThrow(
                () -> workspaceUsers.forEach(workspaceUser -> workspaceUser.validInWorkspace(workspace)));
    }

    @DisplayName("하나의 워크스페이스 내에 있는지 검증 - 실패 / 다른 워크스페이스에 속한 유저가 있을 경우")
    @Test
    void valid_in_workspace_fail_not_in_equal_workspace() throws Exception {

        // given
        workspace = WORKSPACE_1;
        workspaceUsers = new ArrayList<>(Arrays.asList(WORKSPACE_USER_1, WORKSPACE_USER_2));

        // when, then
        assertThrows(WorkspaceUsersNotInEqualWorkspaceException.class,
                () -> workspaceUsers.forEach(workspaceUser -> workspaceUser.validInWorkspace(workspace)));
    }

    @DisplayName("워크스페이스 유저 생성 - 성공")
    @Test
    void create_workspace_user_success() throws Exception {

        // given
        String nickname = "테스트";
        boolean admin = true;
        String position = "테스트";
        String profileImageUrl = "https://test.com/123";
        String contactMail = "test@test.com";
        String phone = "01012341234";
        WorkspaceUserInfo info = WorkspaceUserInfo.builder()
                .nickname(nickname)
                .isWorkspaceAdmin(admin)
                .position(position)
                .profileImageUrl(profileImageUrl)
                .contactMail(contactMail)
                .phone(phone)
                .build();
        user = USER;
        workspace = WORKSPACE_1;

        // when
        WorkspaceUser workspaceUser = WorkspaceUser.of(user, workspace, info);

        // then
        assertAll(
                () -> assertEquals(user, workspaceUser.getUser()),
                () -> assertEquals(workspace, workspaceUser.getWorkspace()),
                () -> assertEquals(nickname, workspaceUser.getWorkspaceUserInfo().getNickname()),
                () -> assertTrue(workspaceUser.getWorkspaceUserInfo().isWorkspaceAdmin()),
                () -> assertNotNull(workspaceUser.getWorkspaceUserInfo().getPosition()),
                () -> assertNotNull(workspaceUser.getWorkspaceUserInfo().getProfileImageUrl()),
                () -> assertNotNull(workspaceUser.getWorkspaceUserInfo().getContactMail()),
                () -> assertNotNull(workspaceUser.getWorkspaceUserInfo().getPhone())
        );
    }

    @DisplayName("팀 등록 - 성공")
    @Test
    void join_team_success() throws Exception {

        // given
        workspaceUser = WORKSPACE_USER_FOR_JOIN_TEAM;
        team = TEAM_1;

        // when
        workspaceUser.joinTeam(team);

        // then
        Team joinedTeam = workspaceUser.getTeam();
        assertEquals(team.getId(), joinedTeam.getId());
    }

    @DisplayName("팀 등록 - 실패 / 이미 팀이 소속된 경우")
    @Test
    void join_team_fail_previous_belong_to_team() throws Exception {

        // given
        workspaceUserWithTeam = WORKSPACE_USER_WITH_TEAM;
        team = TEAM_1;

        // when, then
        assertThrows(PreviousBelongToTeamException.class,
                () -> workspaceUserWithTeam.joinTeam(team));
    }

    @DisplayName("팀 탈퇴 - 성공")
    @Test
    void exit_team_success() throws Exception {

        // given
        workspaceUserWithTeam = WORKSPACE_USER_WITH_TEAM_FOR_EXIT;
        workspaceUserWithTeam.exitTeam();

        // when, then
        assertNull(workspaceUserWithTeam.getTeam());
    }

    @DisplayName("관리자가 아닐 경우 예외 발생 - 성공")
    @Test
    void is_not_admin_do_throw_success() throws Exception {

        // given
        workspaceUser = WORKSPACE_USER_1;

        // when, then
        assertThrows(NotAdminException.class,
                () -> workspaceUser.isAdminOrThrow());
    }

    @DisplayName("관리자일 경우 예외 발생하지 않음 - 성공")
    @Test
    void is_admin_does_not_throw_success() throws Exception {

        // given
        admin = WORKSPACE_USER_ADMIN;

        // when, then
        assertDoesNotThrow(() -> admin.isAdminOrThrow());
    }

    @DisplayName("워크스페이스 유저 정보 수정 - 성공")
    @Test
    void modify_workspace_user_info_success() throws Exception {

        // given
        WorkspaceUser workspaceUser = createWorkspaceUser();
        WorkspaceUserInfo info = WORKSPACE_USER_INFO;

        // when
        workspaceUser.modifyInfo(info);

        // then
        WorkspaceUserInfo after = workspaceUser.getWorkspaceUserInfo();
        assertAll(
                () -> assertEquals(info.getContactMail(), after.getContactMail()),
                () -> assertEquals(info.getNickname(), after.getNickname()),
                () -> assertEquals(info.getPhone(), after.getPhone()),
                () -> assertEquals(info.getPosition(), after.getPosition()),
                () -> assertEquals(info.getProfileImageUrl(), after.getProfileImageUrl())
        );
    }

    private WorkspaceUser createWorkspaceUser() {
        return WorkspaceUser.builder()
                .id(1L)
                .workspaceUserInfo(WorkspaceUserInfo.builder()
                        .contactMail("beforemodify@test.com")
                        .nickname("테스트으")
                        .phone("01012341234")
                        .position("사원")
                        .profileImageUrl("https://test.test.com")
                        .build())
                .build();
    }
}
