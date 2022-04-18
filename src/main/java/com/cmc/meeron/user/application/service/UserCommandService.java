package com.cmc.meeron.user.application.service;

import com.cmc.meeron.common.exception.user.UserNotFoundException;
import com.cmc.meeron.common.security.AuthUser;
import com.cmc.meeron.user.application.port.in.UserCommandUseCase;
import com.cmc.meeron.user.application.port.out.UserQueryPort;
import com.cmc.meeron.user.application.port.out.UserToWorkspaceUserQueryPort;
import com.cmc.meeron.user.domain.User;
import com.cmc.meeron.workspaceuser.domain.WorkspaceUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
class UserCommandService implements UserCommandUseCase {

    private final UserQueryPort userQueryPort;
    private final UserToWorkspaceUserQueryPort userToWorkspaceUserQueryPort;

    @Override
    public void setName(AuthUser authUser, String name) {
        User user = userQueryPort.findById(authUser.getUserId())
                .orElseThrow(UserNotFoundException::new);
        user.setName(name);
    }

    // TODO: 2022/04/03 kobeomseok95 현재 워크스페이스는 하나만 가질 수 있으므로 지워진 워크스페이스 뒤져서 지우는 방안
    @Override
    public void quit(AuthUser authUser) {
        userQueryPort.findById(authUser.getUserId())
                .ifPresent(user -> {
                    user.quit();
                    WorkspaceUser workspaceUser = userToWorkspaceUserQueryPort.findWithWorkspaceByUserId(user.getId()).get(0);
                    workspaceUser.quit();
                    if (workspaceUser.isAdmin()) {
                        List<WorkspaceUser> deletedWorkspaceUsers = userToWorkspaceUserQueryPort.findByWorkspaceId(workspaceUser.getWorkspace().getId());
                        deletedWorkspaceUsers.forEach(WorkspaceUser::quit);
                    }
                });
    }
}
