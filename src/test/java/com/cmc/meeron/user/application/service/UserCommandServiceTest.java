package com.cmc.meeron.user.application.service;

import com.cmc.meeron.common.security.AuthUser;
import com.cmc.meeron.user.application.port.out.UserQueryPort;
import com.cmc.meeron.user.domain.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static com.cmc.meeron.auth.AuthUserFixture.AUTH_USER;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserCommandServiceTest {

    @Mock UserQueryPort userQueryPort;
    @InjectMocks UserCommandService userCommandService;

    @DisplayName("유저 성함 저장 - 성공")
    @Test
    void set_name_success() throws Exception {

        // given
        AuthUser authUser = AUTH_USER;
        String name = "테스트";
        when(userQueryPort.findById(any()))
                .thenReturn(Optional.of(authUser.getUser()));

        // when
        userCommandService.setName(authUser, name);

        // then
        User user = authUser.getUser();
        assertAll(
                () -> verify(userQueryPort).findById(authUser.getUserId()),
                () -> assertEquals(name, user.getName())
        );
    }
}
