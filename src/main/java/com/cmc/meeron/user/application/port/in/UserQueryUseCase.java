package com.cmc.meeron.user.application.port.in;

import com.cmc.meeron.common.security.AuthUser;
import com.cmc.meeron.user.application.port.in.response.UserResponseDto;

public interface UserQueryUseCase {

    UserResponseDto getMe(AuthUser authUser);

    boolean checkNamedUser(Long userId);

    UserResponseDto getUserByWorkspaceUserId(Long workspaceUserId);
}
