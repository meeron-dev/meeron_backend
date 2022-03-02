package com.cmc.meeron.support.security;

import com.cmc.meeron.auth.domain.AuthUser;
import com.cmc.meeron.auth.domain.TokenRepository;
import com.cmc.meeron.auth.handler.CustomUserDetailsService;
import com.cmc.meeron.auth.handler.RestAccessDeniedHandler;
import com.cmc.meeron.auth.handler.RestAuthenticationEntryPoint;
import com.cmc.meeron.auth.provider.JwtProvider;
import com.cmc.meeron.common.exception.auth.TokenAuthenticationException;
import com.cmc.meeron.user.domain.Role;
import com.cmc.meeron.user.domain.User;
import com.cmc.meeron.user.domain.UserProvider;
import com.cmc.meeron.user.domain.UserRepository;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public abstract class SecuritySupport {

    @MockBean protected RestAuthenticationEntryPoint restAuthenticationEntryPoint;
    @MockBean protected RestAccessDeniedHandler restAccessDeniedHandler;
    @MockBean protected CustomUserDetailsService customUserDetailsService;
    @MockBean protected TokenRepository tokenRepository;
    @MockBean protected JwtProvider jwtProvider;
    @MockBean protected UserRepository userRepository;

    protected void setUpUnAuthenticated() {
        when(jwtProvider.isStartWithBearer(any())).thenReturn(true);
        when(jwtProvider.validateToken(any())).thenThrow(new TokenAuthenticationException());
    }

    protected void setUpAuthenticated() {
        when(jwtProvider.isStartWithBearer(any())).thenReturn(true);
        when(jwtProvider.validateToken(any())).thenReturn(true);
        when(tokenRepository.existsLogoutAccessTokenById(any())).thenReturn(false);
        when(tokenRepository.existsLogoutRefreshTokenById(any())).thenReturn(false);
        when(jwtProvider.getUserEmail(any())).thenReturn("test@gmail.com");
        when(customUserDetailsService.loadUserByUsername(any()))
                .thenReturn(AuthUser.of(
                        User.builder()
                                .id(1L)
                                .email("test@gmail.com")
                                .userProvider(UserProvider.KAKAO)
                                .role(Role.USER)
                                .build()));
    }
}
