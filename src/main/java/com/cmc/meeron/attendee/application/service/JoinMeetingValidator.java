package com.cmc.meeron.attendee.application.service;

import com.cmc.meeron.attendee.application.port.out.AttendeeToWorkspaceUserQueryPort;
import com.cmc.meeron.common.exception.workspace.WorkspaceUsersNotInEqualWorkspaceException;
import com.cmc.meeron.workspace.domain.Workspace;
import com.cmc.meeron.workspaceuser.domain.WorkspaceUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
class JoinMeetingValidator {

    private final AttendeeToWorkspaceUserQueryPort attendeeToWorkspaceUserQueryPort;

    public List<WorkspaceUser> workspaceUsersInEqualWorkspace(List<Long> workspaceUserIds, Workspace workspace) {
        List<WorkspaceUser> attendWorkspaceUsers = attendeeToWorkspaceUserQueryPort.findAllWorkspaceUsersByIds(workspaceUserIds);
        attendWorkspaceUsers.forEach(workspaceUser -> {
            if (!workspaceUser.getWorkspace().equals(workspace))
                throw new WorkspaceUsersNotInEqualWorkspaceException();
        });
        return attendWorkspaceUsers;
    }
}
