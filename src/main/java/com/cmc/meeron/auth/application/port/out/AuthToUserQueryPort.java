package com.cmc.meeron.auth.application.port.out;

import com.cmc.meeron.user.domain.User;

import java.util.Optional;

public interface AuthToUserQueryPort {

    Optional<User> findByEmail(String email);
}
