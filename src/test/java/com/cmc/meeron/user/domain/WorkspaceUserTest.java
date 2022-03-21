package com.cmc.meeron.user.domain;

import com.cmc.meeron.common.exception.workspace.WorkspaceUsersNotInEqualWorkspaceException;
import com.cmc.meeron.workspace.domain.Workspace;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.cmc.meeron.user.UserFixture.USER;
import static com.cmc.meeron.user.WorkspaceUserFixture.WORKSPACE_USER_1;
import static com.cmc.meeron.user.WorkspaceUserFixture.WORKSPACE_USER_2;
import static com.cmc.meeron.workspace.WorkspaceFixture.WORKSPACE_1;
import static org.junit.jupiter.api.Assertions.*;

public class WorkspaceUserTest {

    private Workspace workspace;
    private List<WorkspaceUser> users;
    private User user;

    @BeforeEach
    void setUp() {
        workspace = WORKSPACE_1;
        users = new ArrayList<>(Arrays.asList(WORKSPACE_USER_1));
        user = USER;
    }

    @DisplayName("하나의 워크스페이스 내에 있는지 검증 - 성공")
    @Test
    void valid_in_workspace_success() throws Exception {

        // given, when, then
        assertDoesNotThrow(
                () -> users.forEach(workspaceUser -> workspaceUser.validInWorkspace(workspace)));
    }

    @DisplayName("하나의 워크스페이스 내에 있는지 검증 - 실패 / 다른 워크스페이스에 속한 유저가 있을 경우")
    @Test
    void valid_in_workspace_fail_not_in_equal_workspace() throws Exception {

        // given
        users.add(WORKSPACE_USER_2);

        // when, then
        assertThrows(WorkspaceUsersNotInEqualWorkspaceException.class,
                () -> users.forEach(workspaceUser -> workspaceUser.validInWorkspace(workspace)));
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

        // when
        WorkspaceUser workspaceUser = WorkspaceUser.of(user, workspace, info);

        // then
        assertAll(
                () -> assertEquals(user, workspaceUser.getUser()),
                () -> assertEquals(workspace, workspaceUser.getWorkspace()),
                () -> assertEquals(nickname, workspaceUser.getWorkspaceUserInfo().getNickname()),
                () -> assertEquals(admin, workspaceUser.getWorkspaceUserInfo().isWorkspaceAdmin()),
                () -> assertNotNull(workspaceUser.getWorkspaceUserInfo().getPosition()),
                () -> assertNotNull(workspaceUser.getWorkspaceUserInfo().getProfileImageUrl()),
                () -> assertNotNull(workspaceUser.getWorkspaceUserInfo().getContactMail()),
                () -> assertNotNull(workspaceUser.getWorkspaceUserInfo().getPhone())
        );
    }
}
