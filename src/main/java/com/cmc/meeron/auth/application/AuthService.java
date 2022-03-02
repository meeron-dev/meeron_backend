package com.cmc.meeron.auth.application;

import com.cmc.meeron.auth.application.dto.request.LoginRequestDto;
import com.cmc.meeron.auth.application.dto.response.TokenResponseDto;
import com.cmc.meeron.auth.domain.AuthUser;
import com.cmc.meeron.auth.domain.LogoutAccessToken;
import com.cmc.meeron.auth.domain.LogoutRefreshToken;
import com.cmc.meeron.auth.domain.RefreshToken;
import com.cmc.meeron.auth.domain.TokenRepository;
import com.cmc.meeron.auth.provider.JwtProvider;
import com.cmc.meeron.common.exception.auth.RefreshTokenNotExistException;
import com.cmc.meeron.user.domain.User;
import com.cmc.meeron.user.domain.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
class AuthService implements AuthUseCase {

    private final UserRepository userRepository;
    private final JwtProvider jwtProvider;
    private final TokenRepository tokenRepository;

    @Override
    public TokenResponseDto login(LoginRequestDto loginRequestDto) {
        User user = userRepository.findByEmail(loginRequestDto.getEmail())
                .orElseGet(() -> userRepository.save(User.of(
                        loginRequestDto.getEmail(),
                        loginRequestDto.getNickname(),
                        loginRequestDto.getProvider())));
        AuthUser authUser = AuthUser.of(user);
        String accessToken = jwtProvider.createAccessToken(authUser);
        String refreshToken = createAndSaveRefreshToken(authUser);
        return TokenResponseDto.of(accessToken, refreshToken);
    }

    private String createAndSaveRefreshToken(AuthUser authUser) {
        String refreshToken = jwtProvider.createRefreshToken(authUser);
        tokenRepository.saveRefreshToken(
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

        tokenRepository.saveLogoutAccessToken(logoutAccessToken);
        tokenRepository.saveLogoutRefreshToken(logoutRefreshToken);
    }

    @Override
    public TokenResponseDto reissue(String refreshToken, AuthUser authUser) {
        RefreshToken redisRefreshToken = tokenRepository.findRefreshTokenByUsername(authUser.getUsername())
                .orElseThrow(RefreshTokenNotExistException::new);
        if (jwtProvider.isRemainTimeOverRefreshTokenValidTime(redisRefreshToken.getExpiration())) {
            return TokenResponseDto.of(jwtProvider.createAccessToken(authUser), refreshToken);
        }
        return TokenResponseDto.of(jwtProvider.createAccessToken(authUser), createAndSaveRefreshToken(authUser));
    }
}
