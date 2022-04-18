package com.cmc.meeron.workspaceuser;

import com.cmc.meeron.workspaceuser.domain.WorkspaceUser;
import com.cmc.meeron.workspaceuser.domain.WorkspaceUserInfo;

import static com.cmc.meeron.team.TeamFixture.TEAM_1;
import static com.cmc.meeron.user.UserFixture.USER;
import static com.cmc.meeron.workspace.WorkspaceFixture.WORKSPACE_1;
import static com.cmc.meeron.workspace.WorkspaceFixture.WORKSPACE_2;

public class WorkspaceUserFixture {

    private static final WorkspaceUserInfo WORKSPACE_USER_INFO_1 = WorkspaceUserInfo.builder()
            .profileImageUrl("test")
            .isWorkspaceAdmin(false)
            .contactMail("test1@test.com")
            .nickname("무무")
            .phone("01012341234")
            .position("사원")
            .build();

    public static final WorkspaceUser WORKSPACE_USER_1 = WorkspaceUser.builder()
            .id(59L)
            .workspace(WORKSPACE_1)
            .workspaceUserInfo(WORKSPACE_USER_INFO_1)
            .user(USER)
            .build();

    private static final WorkspaceUserInfo WORKSPACE_USER_INFO_2 = WorkspaceUserInfo.builder()
            .profileImageUrl("")
            .isWorkspaceAdmin(false)
            .contactMail("test2@test.com")
            .nickname("테스트닉네임")
            .phone("01012341235")
            .position("사원")
            .build();

    public static final WorkspaceUser WORKSPACE_USER_2 = WorkspaceUser.builder()
            .id(14L)
            .workspace(WORKSPACE_2)
            .workspaceUserInfo(WORKSPACE_USER_INFO_2)
            .build();

    private static final WorkspaceUserInfo WORKSPACE_USER_INFO_3 = WorkspaceUserInfo.builder()
            .profileImageUrl("")
            .isWorkspaceAdmin(false)
            .contactMail("test10@test.com")
            .nickname("무무무")
            .phone("01012341236")
            .position("사원")
            .build();

    public static final WorkspaceUser WORKSPACE_USER_3 = WorkspaceUser.builder()
            .id(14L)
            .workspace(WORKSPACE_1)
            .workspaceUserInfo(WORKSPACE_USER_INFO_3)
            .build();

    private static final WorkspaceUserInfo WORKSPACE_USER_INFO_4 = WorkspaceUserInfo.builder()
            .profileImageUrl("")
            .isWorkspaceAdmin(false)
            .contactMail("test11@test.com")
            .nickname("누구세요")
            .phone("01012341237")
            .position("사원")
            .build();

    public static final WorkspaceUser WORKSPACE_USER_4 = WorkspaceUser.builder()
            .id(14L)
            .workspace(WORKSPACE_1)
            .workspaceUserInfo(WORKSPACE_USER_INFO_4)
            .build();

    public static final WorkspaceUser WORKSPACE_USER_WITH_TEAM = WorkspaceUser.builder()
            .id(20L)
            .team(TEAM_1)
            .workspace(WORKSPACE_1)
            .build();

    public static final WorkspaceUser WORKSPACE_USER_WITH_TEAM2 = WorkspaceUser.builder()
            .id(20L)
            .team(TEAM_1)
            .workspace(WORKSPACE_1)
            .build();

    private static final WorkspaceUserInfo WORKSPACE_USER_INFO_ADMIN = WorkspaceUserInfo.builder()
            .profileImageUrl("")
            .isWorkspaceAdmin(true)
            .contactMail("test@test.com")
            .nickname("관리자")
            .phone("01012341238")
            .position("관리자")
            .build();

    public static final WorkspaceUser WORKSPACE_USER_ADMIN = WorkspaceUser.builder()
            .id(20L)
            .team(TEAM_1)
            .workspace(WORKSPACE_1)
            .workspaceUserInfo(WORKSPACE_USER_INFO_ADMIN)
            .build();

    public static final WorkspaceUser WORKSPACE_USER_FOR_JOIN_TEAM = WorkspaceUser.builder()
            .id(22L)
            .workspace(WORKSPACE_1)
            .build();

    public static final WorkspaceUser WORKSPACE_USER_FOR_JOIN_TEAM2 = WorkspaceUser.builder()
            .id(22L)
            .workspace(WORKSPACE_1)
            .workspaceUserInfo(WORKSPACE_USER_INFO_2)
            .build();

    public static final WorkspaceUser WORKSPACE_USER_WITH_TEAM_FOR_EXIT = WorkspaceUser.builder()
            .id(23L)
            .team(TEAM_1)
            .workspace(WORKSPACE_1)
            .build();
}
