package com.cmc.meeron.user.adapter.out;

import com.cmc.meeron.auth.application.port.out.AuthToUserCommandPort;
import com.cmc.meeron.user.application.port.out.UserCommandPort;
import com.cmc.meeron.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
class UserCommandRepository implements UserCommandPort,
        AuthToUserCommandPort {

    private final UserJpaRepository userJpaRepository;

    @Override
    public User save(User user) {
        return userJpaRepository.save(user);
    }
}
