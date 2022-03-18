package com.cmc.meeron.auth.application.service;

import com.cmc.meeron.auth.application.port.in.AuthUseCase;
import com.cmc.meeron.auth.application.port.in.request.LoginRequestDto;
import com.cmc.meeron.auth.application.port.in.response.TokenResponseDto;
import com.cmc.meeron.auth.application.port.out.TokenCommandPort;
import com.cmc.meeron.auth.application.port.out.TokenQueryPort;
import com.cmc.meeron.auth.domain.LogoutAccessToken;
import com.cmc.meeron.auth.domain.LogoutRefreshToken;
import com.cmc.meeron.auth.domain.RefreshToken;
import com.cmc.meeron.common.exception.auth.RefreshTokenNotExistException;
import com.cmc.meeron.common.security.AuthUser;
import com.cmc.meeron.common.security.JwtProvider;
import com.cmc.meeron.user.application.port.out.UserCommandPort;
import com.cmc.meeron.user.application.port.out.UserQueryPort;
import com.cmc.meeron.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
class AuthService implements AuthUseCase {

    private final UserQueryPort userQueryPort;
    private final UserCommandPort userCommandPort;
    private final JwtProvider jwtProvider;
    private final TokenQueryPort tokenQueryPort;
    private final TokenCommandPort tokenCommandPort;

    @Override
    public TokenResponseDto login(LoginRequestDto loginRequestDto) {
        User user = userQueryPort.findByEmail(loginRequestDto.getEmail())
                .orElseGet(() -> userCommandPort.save(User.of(
                        loginRequestDto.getEmail(),
                        loginRequestDto.getProvider(),
                        loginRequestDto.getProfileImageUrl())));
        AuthUser authUser = AuthUser.of(user);
        String accessToken = jwtProvider.createAccessToken(authUser);
        String refreshToken = createAndSaveRefreshToken(authUser);
        return TokenResponseDto.of(accessToken, refreshToken);
    }

    private String createAndSaveRefreshToken(AuthUser authUser) {
        String refreshToken = jwtProvider.createRefreshToken(authUser);
        tokenCommandPort.saveRefreshToken(
                RefreshToken.of(authUser.getUsername(),
                        refreshToken,
                        jwtProvider.getRemainingMilliSecondsFromToken(refreshToken)));
        return refreshToken;
    }

    @Override
    public void logout(String accessToken, String refreshToken) {
        LogoutAccessToken logoutAccessToken =
                LogoutAccessToken.of(accessToken, jwtProvider.getRemainingMilliSecondsFromToken(accessToken));
        LogoutRefreshToken logoutRefreshToken =
                LogoutRefreshToken.of(refreshToken, jwtProvider.getRemainingMilliSecondsFromToken(refreshToken));

        tokenCommandPort.saveLogoutAccessToken(logoutAccessToken);
        tokenCommandPort.saveLogoutRefreshToken(logoutRefreshToken);
    }

    @Override
    public TokenResponseDto reissue(String refreshToken, AuthUser authUser) {
        RefreshToken redisRefreshToken = tokenQueryPort.findRefreshTokenByUsername(authUser.getUsername())
                .orElseThrow(RefreshTokenNotExistException::new);
        if (jwtProvider.isRemainTimeOverRefreshTokenValidTime(redisRefreshToken.getExpiration())) {
            return TokenResponseDto.of(jwtProvider.createAccessToken(authUser), refreshToken);
        }
        return TokenResponseDto.of(jwtProvider.createAccessToken(authUser), createAndSaveRefreshToken(authUser));
    }
}
