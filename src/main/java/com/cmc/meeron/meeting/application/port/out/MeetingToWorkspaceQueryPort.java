package com.cmc.meeron.meeting.application.port.out;

import com.cmc.meeron.workspace.domain.Workspace;

import java.util.Optional;

public interface MeetingToWorkspaceQueryPort {

    Optional<Workspace> findById(Long workspaceId);
}
