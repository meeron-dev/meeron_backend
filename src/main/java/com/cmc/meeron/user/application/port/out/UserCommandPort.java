package com.cmc.meeron.user.application.port.out;

import com.cmc.meeron.user.domain.User;
import com.cmc.meeron.user.domain.WorkspaceUser;

public interface UserCommandPort {

    User save(User user);

    WorkspaceUser saveWorkspaceUser(WorkspaceUser workspaceUser);
}
