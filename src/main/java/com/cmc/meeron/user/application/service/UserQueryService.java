package com.cmc.meeron.user.application.service;

import com.cmc.meeron.common.exception.user.UserNotFoundException;
import com.cmc.meeron.common.security.AuthUser;
import com.cmc.meeron.user.application.port.in.UserQueryUseCase;
import com.cmc.meeron.user.application.port.in.response.UserResponseDto;
import com.cmc.meeron.user.application.port.out.UserQueryPort;
import com.cmc.meeron.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
class UserQueryService implements UserQueryUseCase {

    private final UserQueryPort userQueryPort;

    @Override
    public UserResponseDto getMe(AuthUser authUser) {
        return UserResponseDto.from(authUser.getUser());
    }

    @Override
    public boolean checkNamedUser(Long userId) {
        User user = userQueryPort.findById(userId)
                .orElseThrow(UserNotFoundException::new);
        return StringUtils.hasText(user.getName());
    }

    @Override
    public UserResponseDto getUserByWorkspaceUserId(Long workspaceUserId) {
        User user = userQueryPort.findByWorkspaceUserId(workspaceUserId)
                .orElseThrow(UserNotFoundException::new);
        return UserResponseDto.from(user);
    }
}
