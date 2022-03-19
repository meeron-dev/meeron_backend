package com.cmc.meeron.user.application.service;

import com.cmc.meeron.common.exception.user.UserNotFoundException;
import com.cmc.meeron.common.security.AuthUser;
import com.cmc.meeron.user.application.port.in.UserCommandUseCase;
import com.cmc.meeron.user.application.port.out.UserCommandPort;
import com.cmc.meeron.user.application.port.out.UserQueryPort;
import com.cmc.meeron.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
class UserCommandService implements UserCommandUseCase {

    private final UserQueryPort userQueryPort;
    private final UserCommandPort userCommandPort;

    @Override
    public void setName(AuthUser authUser, String name) {
        User user = userQueryPort.findById(authUser.getUserId())
                .orElseThrow(UserNotFoundException::new);
        user.setName(name);
    }
}
