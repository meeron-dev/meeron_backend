package com.cmc.meeron.user.application.port.in;

import com.cmc.meeron.common.security.AuthUser;

public interface UserCommandUseCase {

    void setName(AuthUser authUser, String name);
}
