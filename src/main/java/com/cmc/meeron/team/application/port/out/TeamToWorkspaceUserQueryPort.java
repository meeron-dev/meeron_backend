package com.cmc.meeron.team.application.port.out;

import com.cmc.meeron.workspaceuser.domain.WorkspaceUser;

import java.util.List;

public interface TeamToWorkspaceUserQueryPort {

    List<WorkspaceUser> findByTeamId(Long teamId);
}
