package com.cmc.meeron.user.application.port.out;

import com.cmc.meeron.user.domain.User;

public interface UserCommandPort {

    User save(User user);
}
