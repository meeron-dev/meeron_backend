package com.cmc.meeron.user.domain.repository;

import com.cmc.meeron.user.domain.User;

import java.util.Optional;

public interface UserRepository {

    Optional<User> findByEmail(String email);

    User save(User user);
}
