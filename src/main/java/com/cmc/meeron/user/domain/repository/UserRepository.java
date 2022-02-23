package com.cmc.meeron.user.domain.repository;

import com.cmc.meeron.user.domain.User;
import com.cmc.meeron.user.domain.WorkspaceUser;

import java.util.List;
import java.util.Optional;

public interface UserRepository {

    Optional<User> findByEmail(String email);

    User save(User user);

    List<WorkspaceUser> findMyWorkspaceUsers(Long userId);

    Optional<WorkspaceUser> findWorkspaceUserById(Long workspaceUserId);
}
