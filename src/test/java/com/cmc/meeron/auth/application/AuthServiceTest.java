package com.cmc.meeron.auth.application;

import com.cmc.meeron.auth.application.dto.request.LoginRequestDto;
import com.cmc.meeron.auth.application.dto.response.TokenResponseDto;
import com.cmc.meeron.auth.domain.AuthUser;
import com.cmc.meeron.auth.domain.LogoutAccessToken;
import com.cmc.meeron.auth.domain.LogoutRefreshToken;
import com.cmc.meeron.auth.domain.repository.LogoutAccessTokenRepository;
import com.cmc.meeron.auth.domain.repository.LogoutRefreshTokenRepository;
import com.cmc.meeron.auth.provider.JwtProvider;
import com.cmc.meeron.user.domain.Role;
import com.cmc.meeron.user.domain.User;
import com.cmc.meeron.user.domain.UserProvider;
import com.cmc.meeron.user.domain.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    private final String ACCESS_TOKEN = "testAccessToken";
    private final String REFRESH_TOKEN = "testRefreshToken";

    @Mock UserRepository userRepository;
    @Mock JwtProvider jwtProvider;
    @Mock LogoutAccessTokenRepository logoutAccessTokenRepository;
    @Mock LogoutRefreshTokenRepository logoutRefreshTokenRepository;
    @InjectMocks AuthService authService;

    @DisplayName("로그인 - 성공 / 회원가입이 되지 않은 유저일 경우")
    @Test
    void login_success_notExistsUser() throws Exception {

        // given
        LoginRequestDto kakaoLoginRequest = mockKakaoLoginRequest();
        when(userRepository.findByEmail(kakaoLoginRequest.getEmail()))
                .thenReturn(Optional.empty());
        mockJwtProvider();

        // when
        TokenResponseDto tokenResponseDto = authService.login(kakaoLoginRequest);

        // then
        assertAll(
                () -> verify(userRepository).findByEmail(kakaoLoginRequest.getEmail()),
                () -> verify(userRepository).save(any(User.class)),
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
                .name("고범석")
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
        when(userRepository.findByEmail(kakaoLoginRequest.getEmail()))
                .thenReturn(Optional.of(mockUser));
        mockJwtProvider();

        // when
        TokenResponseDto tokenResponseDto = authService.login(kakaoLoginRequest);

        // then
        assertAll(
                () -> verify(userRepository).findByEmail(kakaoLoginRequest.getEmail()),
                () -> verify(userRepository, times(0)).save(any(User.class)),
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
                () -> verify(logoutAccessTokenRepository).save(any(LogoutAccessToken.class)),
                () -> verify(logoutRefreshTokenRepository).save(any(LogoutRefreshToken.class))
        );
    }

    @DisplayName("토큰 재발급 - 성공")
    @Test
    void reissue_success() throws Exception {

        // given
        AuthUser authUser = AuthUser.of(mockUser());
        mockJwtProvider();

        // when
        TokenResponseDto reissuedTokenResponse = authService.reissue(authUser);

        // then
        assertAll(
                () -> verify(jwtProvider).createAccessToken(authUser),
                () -> verify(jwtProvider).createRefreshToken(authUser)
        );
    }
}
