package com.cmc.meeron.user.application.port.in;

import com.cmc.meeron.common.security.AuthUser;
import com.cmc.meeron.user.application.port.in.response.MeResponseDto;

public interface UserQueryUseCase {

    MeResponseDto getMe(AuthUser authUser);

    boolean checkNamedUser(Long userId);
}
