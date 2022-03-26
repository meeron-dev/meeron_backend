package com.cmc.meeron.user.application.service;

import com.cmc.meeron.common.exception.user.UserNotFoundException;
import com.cmc.meeron.common.security.AuthUser;
import com.cmc.meeron.user.application.port.in.UserQueryUseCase;
import com.cmc.meeron.user.application.port.in.response.MeResponseDto;
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
    public MeResponseDto getMe(AuthUser authUser) {
        return MeResponseDto.fromUser(authUser.getUser());
    }

    @Override
    public boolean checkNamedUser(Long userId) {
        User user = userQueryPort.findById(userId)
                .orElseThrow(UserNotFoundException::new);
        return StringUtils.hasText(user.getName());
    }
}
