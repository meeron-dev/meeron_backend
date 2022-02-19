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

    @Override
    public TokenResponseDto login(LoginRequestDto loginRequestDto) {
        User user = userRepository.findByEmail(loginRequestDto.getEmail())
                .orElseGet(() -> save(loginRequestDto));
        AuthUser authUser = AuthUser.of(user);
        String accessToken = jwtProvider.createAccessToken(authUser);
        String refreshToken = jwtProvider.createRefreshToken(authUser);
        return TokenResponseDto.of(accessToken, refreshToken);
    }

    private User save(LoginRequestDto loginRequestDto) {
        return userRepository.save(User.builder()
                .email(loginRequestDto.getEmail())
                .role(Role.USER)
                .userProvider(UserProvider.valueOf(loginRequestDto.getProvider().toUpperCase()))
                .name("")
                .nickname(loginRequestDto.getNickname())
                .build());
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
    public TokenResponseDto reissue(AuthUser authUser) {
        String reissuedAccessToken = jwtProvider.createAccessToken(authUser);
        String reissuedRefreshToken = jwtProvider.createRefreshToken(authUser);
        return TokenResponseDto.of(reissuedAccessToken, reissuedRefreshToken);
    }
}
