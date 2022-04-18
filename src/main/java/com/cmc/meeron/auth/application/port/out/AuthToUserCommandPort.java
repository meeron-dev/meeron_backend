package com.cmc.meeron.auth.application.port.out;

import com.cmc.meeron.user.domain.User;

public interface AuthToUserCommandPort {

    User save(User user);
}
