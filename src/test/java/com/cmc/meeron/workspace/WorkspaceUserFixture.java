package com.cmc.meeron.workspace;

import com.cmc.meeron.workspace.domain.WorkspaceUser;
import com.cmc.meeron.workspace.domain.WorkspaceUserInfo;

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
}
