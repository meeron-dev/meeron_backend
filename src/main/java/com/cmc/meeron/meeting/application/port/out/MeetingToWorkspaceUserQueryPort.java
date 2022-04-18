package com.cmc.meeron.meeting.application.port.out;

import com.cmc.meeron.workspaceuser.domain.WorkspaceUser;

import java.util.List;
import java.util.Optional;

public interface MeetingToWorkspaceUserQueryPort {

    List<WorkspaceUser> findAllWorkspaceUsersByIds(List<Long> workspaceUserIds);

    Optional<WorkspaceUser> findByUserWorkspaceId(Long userId, Long workspaceId);

    List<WorkspaceUser> findMyWorkspaceUsers(Long userId);
}
