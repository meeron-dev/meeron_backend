package com.cmc.meeron.auth.application;

import com.cmc.meeron.auth.application.dto.request.LoginRequestDto;
import com.cmc.meeron.auth.application.dto.response.TokenResponseDto;
import com.cmc.meeron.auth.domain.AuthUser;
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
                .name(loginRequestDto.getName())
                .build());
    }
}
