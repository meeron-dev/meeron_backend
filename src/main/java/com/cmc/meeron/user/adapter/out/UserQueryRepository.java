package com.cmc.meeron.user.adapter.out;

import com.cmc.meeron.auth.application.port.out.AuthToUserQueryPort;
import com.cmc.meeron.user.application.port.out.UserQueryPort;
import com.cmc.meeron.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
class UserQueryRepository implements UserQueryPort,
        AuthToUserQueryPort {

    private final UserJpaRepository userJpaRepository;

    @Override
    public Optional<User> findByEmail(String email) {
        return userJpaRepository.findByEmail(email);
    }

    @Override
    public Optional<User> findById(Long userId) {
        return userJpaRepository.findById(userId);
    }

    @Override
    public Optional<User> findByWorkspaceUserId(Long workspaceUserId) {
        return userJpaRepository.findByWorkspaceUserId(workspaceUserId);
    }
}
