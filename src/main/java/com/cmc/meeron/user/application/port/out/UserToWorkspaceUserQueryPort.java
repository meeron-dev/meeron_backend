package com.cmc.meeron.user.application.port.out;

import com.cmc.meeron.workspaceuser.domain.WorkspaceUser;

import java.util.List;

public interface UserToWorkspaceUserQueryPort {

    List<WorkspaceUser> findWithWorkspaceByUserId(Long userId);

    List<WorkspaceUser> findByWorkspaceId(Long workspaceId);
}
