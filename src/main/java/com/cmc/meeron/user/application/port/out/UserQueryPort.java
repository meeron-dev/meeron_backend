package com.cmc.meeron.user.application.port.out;

import com.cmc.meeron.user.domain.User;

import java.util.Optional;

public interface UserQueryPort {

    Optional<User> findByEmail(String email);

    Optional<User> findById(Long userId);
}
