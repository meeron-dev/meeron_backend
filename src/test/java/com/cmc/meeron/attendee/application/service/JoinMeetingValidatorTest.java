package com.cmc.meeron.attendee.application.service;

import com.cmc.meeron.common.exception.workspace.WorkspaceUsersNotInEqualWorkspaceException;
import com.cmc.meeron.workspace.application.port.out.WorkspaceUserQueryPort;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static com.cmc.meeron.workspace.WorkspaceFixture.WORKSPACE_1;
import static com.cmc.meeron.workspace.WorkspaceUserFixture.WORKSPACE_USER_1;
import static com.cmc.meeron.workspace.WorkspaceUserFixture.WORKSPACE_USER_2;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class JoinMeetingValidatorTest {

    @Mock
    WorkspaceUserQueryPort workspaceUserQueryPort;
    @InjectMocks
    JoinMeetingValidator joinMeetingValidator;

    @DisplayName("워크스페이스 유저 검증 - 성공 / 워크스페이스 유저가 다른 워크스페이스에 있을 경우")
    @Test
    void workspace_users_in_equal_workspace_success() throws Exception {

        // given
        when(workspaceUserQueryPort.findAllWorkspaceUsersByIds(any()))
                .thenReturn(List.of(WORKSPACE_USER_1, WORKSPACE_USER_2));

        // when, then
        assertThrows(WorkspaceUsersNotInEqualWorkspaceException.class,
                () -> joinMeetingValidator.workspaceUsersInEqualWorkspace(List.of(WORKSPACE_USER_1.getId(),
                        WORKSPACE_USER_2.getId()),
                        WORKSPACE_1));
    }
}