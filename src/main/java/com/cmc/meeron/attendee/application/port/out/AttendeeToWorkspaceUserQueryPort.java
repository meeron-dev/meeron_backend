package com.cmc.meeron.attendee.application.port.out;

import com.cmc.meeron.workspaceuser.domain.WorkspaceUser;

import java.util.List;

public interface AttendeeToWorkspaceUserQueryPort {

    List<WorkspaceUser> findAllWorkspaceUsersByIds(List<Long> workspaceUserIds);
}
