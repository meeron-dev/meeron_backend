package com.cmc.meeron.attendee.application.service;

import com.cmc.meeron.common.exception.workspace.WorkspaceUsersNotInEqualWorkspaceException;
import com.cmc.meeron.workspace.application.port.out.WorkspaceUserQueryPort;
import com.cmc.meeron.workspace.domain.Workspace;
import com.cmc.meeron.workspace.domain.WorkspaceUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@RequiredArgsConstructor
@Transactional
class JoinMeetingValidator {

    private final WorkspaceUserQueryPort workspaceUserQueryPort;

    public List<WorkspaceUser> workspaceUsersInEqualWorkspace(List<Long> workspaceUserIds, Workspace workspace) {
        List<WorkspaceUser> attendWorkspaceUsers = workspaceUserQueryPort.findAllWorkspaceUsersByIds(workspaceUserIds);
        attendWorkspaceUsers.forEach(workspaceUser -> {
            if (!workspaceUser.getWorkspace().equals(workspace))
                throw new WorkspaceUsersNotInEqualWorkspaceException();
        });
        return attendWorkspaceUsers;
    }
}
