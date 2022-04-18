package com.cmc.meeron.auth.application.service;

import com.cmc.meeron.auth.application.port.in.request.LoginRequestDto;
import com.cmc.meeron.auth.application.port.in.response.TokenResponseDto;
import com.cmc.meeron.auth.application.port.out.AuthToUserCommandPort;
import com.cmc.meeron.auth.application.port.out.AuthToUserQueryPort;
import com.cmc.meeron.auth.application.port.out.TokenCommandPort;
import com.cmc.meeron.auth.application.port.out.TokenQueryPort;
import com.cmc.meeron.auth.domain.LogoutAccessToken;
import com.cmc.meeron.auth.domain.LogoutRefreshToken;
import com.cmc.meeron.auth.domain.RefreshToken;
import com.cmc.meeron.common.exception.auth.RefreshTokenNotExistException;
import com.cmc.meeron.common.security.AuthUser;
import com.cmc.meeron.common.security.JwtProvider;
import com.cmc.meeron.user.domain.Role;
import com.cmc.meeron.user.domain.User;
import com.cmc.meeron.user.domain.UserProvider;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    private final String ACCESS_TOKEN = "testAccessToken";
    private final String REFRESH_TOKEN = "testRefreshToken";
    private final String RENEWAL_ACCESS_TOKEN = "refreshedTestAccessToken";
    private final String RENEWAL_REFRESH_TOKEN = "refreshedTestRefreshToken";

    @Mock
    AuthToUserQueryPort authToUserQueryPort;
    @Mock
    AuthToUserCommandPort authToUserCommandPort;
    @Mock
    JwtProvider jwtProvider;
    @Mock
    TokenQueryPort tokenQueryPort;
    @Mock
    TokenCommandPort tokenCommandPort;
    @InjectMocks
    AuthService authService;

    @DisplayName("로그인 - 성공 / 회원가입이 되지 않은 유저일 경우")
    @Test
    void login_success_notExistsUser() throws Exception {

        // given
        LoginRequestDto kakaoLoginRequest = mockKakaoLoginRequest();
        when(authToUserQueryPort.findByEmail(kakaoLoginRequest.getEmail())
                .orElseGet(() -> authToUserCommandPort.save(any())))
                .thenReturn(mockUser());
        mockJwtProvider();

        // when
        TokenResponseDto tokenResponseDto = authService.login(kakaoLoginRequest);

        // then
        assertAll(
                () -> verify(authToUserCommandPort).save(any(User.class)),
                () -> verify(tokenCommandPort).saveRefreshToken(any(RefreshToken.class)),
                () -> verify(jwtProvider).createAccessToken(any(AuthUser.class)),
                () -> verify(jwtProvider).createRefreshToken(any(AuthUser.class))
        );
    }

    private void mockJwtProvider() {
        when(jwtProvider.createAccessToken(any())).thenReturn(ACCESS_TOKEN);
        when(jwtProvider.createAccessToken(any())).thenReturn(REFRESH_TOKEN);
    }

    private LoginRequestDto mockKakaoLoginRequest() {
        return LoginRequestDto.builder()
                .email("test@naver.com")
                .nickname("고범석")
                .profileImageUrl("https://test.test.com/12341234")
                .provider("KAKAO")
                .build();
    }

    private User mockUser() {
        return User.builder()
                .id(1L)
                .email("test@naver.com")
                .name("고범석")
                .profileImageUrl("https://test.test.com/12341234")
                .userProvider(UserProvider.KAKAO)
                .role(Role.USER)
                .build();
    }

    @DisplayName("로그인 - 성공 / 이미 회원가입한 유저인 경우")
    @Test
    void login_success_existsUser() throws Exception {

        // given
        LoginRequestDto kakaoLoginRequest = mockKakaoLoginRequest();
        User mockUser = mockUser();
        when(authToUserQueryPort.findByEmail(kakaoLoginRequest.getEmail()))
                .thenReturn(Optional.of(mockUser));
        mockJwtProvider();

        // when
        TokenResponseDto tokenResponseDto = authService.login(kakaoLoginRequest);

        // then
        assertAll(
                () -> verify(authToUserQueryPort).findByEmail(kakaoLoginRequest.getEmail()),
                () -> verify(authToUserCommandPort, times(0)).save(any(User.class)),
                () -> verify(jwtProvider).createAccessToken(any(AuthUser.class)),
                () -> verify(jwtProvider).createRefreshToken(any(AuthUser.class))
        );
    }

    @DisplayName("로그아웃 - 성공")
    @Test
    void logout_success() throws Exception {

        // given
        String accessToken = "testAccessToken";
        String refreshToken = "testRefreshToken";

        // when
        authService.logout(accessToken, refreshToken);

        // then
        assertAll(
                () -> verify(tokenCommandPort).saveLogoutAccessToken(any(LogoutAccessToken.class)),
                () -> verify(tokenCommandPort).saveLogoutRefreshToken(any(LogoutRefreshToken.class))
        );
    }

    @DisplayName("토큰 재발급 - 성공 / 재발급 유효 기간이 지나지 않았을 경우 Access Token만 발급")
    @Test
    void reissue_success_before_refresh_token_valid_time() throws Exception {

        // given
        User user = mockUser();
        AuthUser authUser = AuthUser.of(user);
        RefreshToken refreshToken = RefreshToken.of(user.getEmail(), REFRESH_TOKEN, 60 * 60 * 24 * 4L);
        mockFindRefreshTokenByUsername(user, refreshToken);
        when(jwtProvider.isRemainTimeOverRefreshTokenValidTime(refreshToken.getExpiration()))
                .thenReturn(true);
        when(jwtProvider.createAccessToken(any())).thenReturn(RENEWAL_ACCESS_TOKEN);

        // when
        TokenResponseDto reissuedTokenResponse = authService.reissue(REFRESH_TOKEN, authUser);

        // then
        assertAll(
                () -> verify(tokenQueryPort).findRefreshTokenByUsername(user.getEmail()),
                () -> assertEquals(REFRESH_TOKEN, reissuedTokenResponse.getRefreshToken())
        );
    }

    private void mockFindRefreshTokenByUsername(User user, RefreshToken refreshToken) {
        when(tokenQueryPort.findRefreshTokenByUsername(user.getEmail()))
                .thenReturn(Optional.of(refreshToken));
    }

    @DisplayName("토큰 재발급 - 성공 / 재발급 유효 기간이 지났을 경우 Access Token, Refresh Token 재발급")
    @Test
    void reissue_success_after_refresh_token_valid_time() throws Exception {

        // given
        User user = mockUser();
        AuthUser authUser = AuthUser.of(user);
        RefreshToken refreshToken = RefreshToken.of(user.getEmail(), REFRESH_TOKEN, 60 * 60 * 24 * 2L);
        mockFindRefreshTokenByUsername(user, refreshToken);
        when(jwtProvider.isRemainTimeOverRefreshTokenValidTime(refreshToken.getExpiration()))
                .thenReturn(false);
        when(jwtProvider.createAccessToken(any())).thenReturn(RENEWAL_ACCESS_TOKEN);
        when(jwtProvider.createRefreshToken(any())).thenReturn(RENEWAL_REFRESH_TOKEN);

        // when
        TokenResponseDto reissuedTokenResponse = authService.reissue(REFRESH_TOKEN, authUser);

        // then
        assertAll(
                () -> verify(tokenQueryPort).findRefreshTokenByUsername(user.getEmail()),
                () -> verify(tokenCommandPort).saveRefreshToken(any(RefreshToken.class)),
                () -> assertNotEquals(REFRESH_TOKEN, reissuedTokenResponse.getRefreshToken())
        );
    }

    @DisplayName("토큰 재발급 - 실패 / Redis에 RefreshToken이 없을 경우")
    @Test
    void reissue_failed_caused_by_not_exist_from_redis() throws Exception {

        // given
        User user = mockUser();
        AuthUser authUser = AuthUser.of(user);
        when(tokenQueryPort.findRefreshTokenByUsername(user.getEmail()))
                .thenReturn(Optional.empty());

        // when, then
        assertThrows(RefreshTokenNotExistException.class,
                () -> authService.reissue(REFRESH_TOKEN, authUser));
    }
}
