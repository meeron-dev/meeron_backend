package com.cmc.meeron.user.adapter.out;

import com.cmc.meeron.user.application.port.out.UserCommandPort;
import com.cmc.meeron.user.domain.User;
import com.cmc.meeron.user.domain.WorkspaceUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
class UserCommandRepository implements UserCommandPort {

    private final UserJpaRepository userJpaRepository;
    private final WorkspaceUserJpaRepository workspaceUserJpaRepository;

    @Override
    public User save(User user) {
        return userJpaRepository.save(user);
    }

    @Override
    public WorkspaceUser saveWorkspaceUser(WorkspaceUser workspaceUser) {
        return workspaceUserJpaRepository.save(workspaceUser);
    }
}
