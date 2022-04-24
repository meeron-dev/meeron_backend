package com.cmc.meeron.team.application.port.out;

import com.cmc.meeron.workspaceuser.domain.WorkspaceUser;

import java.util.List;
import java.util.Optional;

public interface TeamToWorkspaceUserQueryPort {

    List<WorkspaceUser> findByTeamId(Long teamId);

    List<WorkspaceUser> findAllWorkspaceUsersByIds(List<Long> workspaceUserIds);

    Optional<WorkspaceUser> findById(Long workspaceUserId);
}
