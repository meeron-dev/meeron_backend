package com.cmc.meeron.user.domain.repository;

import com.cmc.meeron.user.domain.User;
import com.cmc.meeron.user.domain.WorkspaceUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
class UserRepositoryImpl implements UserRepository {

    private final UserJpaRepository userJpaRepository;
    private final WorkspaceUserJpaRepository workspaceUserJpaRepository;

    @Override
    public Optional<User> findByEmail(String email) {
        return userJpaRepository.findByEmail(email);
    }

    @Override
    public User save(User user) {
        return userJpaRepository.save(user);
    }

    @Override
    public List<WorkspaceUser> findMyWorkspaceUsers(Long userId) {
        return workspaceUserJpaRepository.findByUserId(userId);
    }

    @Override
    public Optional<WorkspaceUser> findWorkspaceUserById(Long workspaceUserId) {
        return workspaceUserJpaRepository.findById(workspaceUserId);
    }
}
