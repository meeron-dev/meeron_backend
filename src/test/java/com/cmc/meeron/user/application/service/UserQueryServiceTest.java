package com.cmc.meeron.user.application.service;

import com.cmc.meeron.common.exception.user.UserNotFoundException;
import com.cmc.meeron.common.security.AuthUser;
import com.cmc.meeron.user.application.port.in.response.UserResponseDto;
import com.cmc.meeron.user.application.port.out.UserQueryPort;
import com.cmc.meeron.user.domain.Role;
import com.cmc.meeron.user.domain.User;
import com.cmc.meeron.user.domain.UserProvider;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static com.cmc.meeron.user.UserFixture.NOT_NAMED_USER;
import static com.cmc.meeron.user.UserFixture.USER;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserQueryServiceTest {

    @Mock
    UserQueryPort userQueryPort;
    @InjectMocks
    UserQueryService userQueryService;

    private User user;
    private User notNamedUser;

    @BeforeEach
    void setUp() {
        user = USER;
        notNamedUser = NOT_NAMED_USER;
    }

    @DisplayName("회원 정보 가져오기 - 성공")
    @Test
    void get_me_success() throws Exception {

        // given
        User user = createUser();
        AuthUser authUser = AuthUser.of(user);

        // when
        UserResponseDto responseDto = userQueryService.getMe(authUser);

        // then
        assertAll(
                () -> assertEquals(user.getId(), responseDto.getUserId()),
                () -> assertEquals(user.getEmail(), responseDto.getLoginEmail()),
                () -> assertEquals(user.getName(), responseDto.getName()),
                () -> assertEquals(user.getProfileImageUrl(), responseDto.getProfileImageUrl())
        );
    }

    private User createUser() {
        return User.builder()
                .id(1L)
                .email("test@gmail.com")
                .role(Role.USER)
                .userProvider(UserProvider.KAKAO)
                .name("테스트")
                .profileImageUrl(null)
                .build();
    }

    @DisplayName("사용자가 이름을 입력했는지 체크 - 성공 / 입력했을경우")
    @Test
    void check_named_user_success_named() throws Exception {

        // given
        when(userQueryPort.findById(any()))
                .thenReturn(Optional.of(user));

        // when
        boolean result = userQueryService.checkNamedUser(1L);

        // then
        assertAll(
                () -> verify(userQueryPort).findById(1L),
                () -> assertTrue(result)
        );
    }

    @DisplayName("사용자가 이름을 입력했는지 체크 - 성공 / 입력하지 않았을경우")
    @Test
    void check_named_user_success_not_named() throws Exception {

        // given
        when(userQueryPort.findById(any()))
                .thenReturn(Optional.of(notNamedUser));

        // when
        boolean result = userQueryService.checkNamedUser(1L);

        // then
        assertAll(
                () -> verify(userQueryPort).findById(1L),
                () -> assertFalse(result)
        );
    }

    @DisplayName("사용자가 이름을 입력했는지 체크 - 실패 / 유저가 존재하지 않을 경우")
    @Test
    void check_entered_user_name_fail_not_found_user() throws Exception {

        // given
        when(userQueryPort.findById(any()))
                .thenReturn(Optional.empty());

        // when, then
        assertThrows(UserNotFoundException.class,
                () -> userQueryService.checkNamedUser(1L));
    }

    @DisplayName("워크스페이스 유저의 유저 조회 - 실패 / 존재하지 않을 경우")
    @Test
    void get_workspace_user_user_fail_not_found() throws Exception {

        // given
        when(userQueryPort.findByWorkspaceUserId(any()))
                .thenReturn(Optional.empty());

        // when, then
        assertThrows(UserNotFoundException.class,
                () -> userQueryService.getUserByWorkspaceUserId(1L));
    }

    @DisplayName("워크스페이스 유저의 유저 조회 - 성공")
    @Test
    void get_workspace_user_user_success() throws Exception {

        // given
        when(userQueryPort.findByWorkspaceUserId(any()))
                .thenReturn(Optional.of(USER));

        // when
        UserResponseDto responseDto = userQueryService.getUserByWorkspaceUserId(1L);

        // then
        assertAll(
                () -> verify(userQueryPort).findByWorkspaceUserId(1L),
                () -> Assertions.assertThat(responseDto)
                        .usingRecursiveComparison()
                        .isEqualTo(UserResponseDto.from(USER))
        );
    }
}
