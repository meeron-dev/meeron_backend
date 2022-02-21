package com.cmc.meeron.auth.application;

import com.cmc.meeron.auth.application.dto.request.LoginRequestDto;
import com.cmc.meeron.auth.application.dto.response.TokenResponseDto;
import com.cmc.meeron.auth.domain.AuthUser;
import com.cmc.meeron.auth.domain.LogoutAccessToken;
import com.cmc.meeron.auth.domain.LogoutRefreshToken;
import com.cmc.meeron.auth.domain.RefreshToken;
import com.cmc.meeron.auth.domain.repository.LogoutAccessTokenRepository;
import com.cmc.meeron.auth.domain.repository.LogoutRefreshTokenRepository;
import com.cmc.meeron.auth.domain.repository.RefreshTokenRepository;
import com.cmc.meeron.auth.provider.JwtProvider;
import com.cmc.meeron.common.exception.auth.RefreshTokenNotExistException;
import com.cmc.meeron.user.domain.User;
import com.cmc.meeron.user.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
class AuthService implements AuthUseCase{

    private final UserRepository userRepository;
    private final JwtProvider jwtProvider;
    private final LogoutAccessTokenRepository logoutAccessTokenRepository;
    private final LogoutRefreshTokenRepository logoutRefreshTokenRepository;
    private final RefreshTokenRepository refreshTokenRepository;

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
        refreshTokenRepository.save(
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

        logoutAccessTokenRepository.save(logoutAccessToken);
        logoutRefreshTokenRepository.save(logoutRefreshToken);
    }

    @Override
    public TokenResponseDto reissue(String accessToken, String refreshToken, AuthUser authUser) {
        RefreshToken redisRefreshToken = refreshTokenRepository.findById(authUser.getUsername())
                .orElseThrow(RefreshTokenNotExistException::new);
        if (jwtProvider.isRemainTimeOverRefreshTokenValidTime(redisRefreshToken.getExpiration())) {
            return TokenResponseDto.of(jwtProvider.createAccessToken(authUser), refreshToken);
        }

        String createdRefreshToken = createAndSaveRefreshToken(authUser);
        return TokenResponseDto.of(jwtProvider.createAccessToken(authUser), createdRefreshToken);
    }
}
