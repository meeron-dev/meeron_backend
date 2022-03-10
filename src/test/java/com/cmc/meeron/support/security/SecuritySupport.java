package com.cmc.meeron.support.security;

import com.cmc.meeron.auth.application.port.out.TokenQueryPort;
import com.cmc.meeron.common.exception.auth.AuthErrorCode;
import com.cmc.meeron.common.exception.auth.TokenAuthenticationException;
import com.cmc.meeron.common.exception.auth.TokenException;
import com.cmc.meeron.common.security.*;
import com.cmc.meeron.user.application.port.out.UserQueryPort;
import com.cmc.meeron.user.domain.Role;
import com.cmc.meeron.user.domain.User;
import com.cmc.meeron.user.domain.UserProvider;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public abstract class SecuritySupport {

    @MockBean protected RestAuthenticationEntryPoint restAuthenticationEntryPoint;
    @MockBean protected RestAccessDeniedHandler restAccessDeniedHandler;
    @MockBean protected CustomUserDetailsService customUserDetailsService;
    @MockBean protected TokenQueryPort tokenQueryPort;
    @MockBean protected JwtProvider jwtProvider;
    @MockBean protected UserQueryPort userQueryPort;

    protected void setUpUnAuthenticated() {
        when(jwtProvider.isStartWithBearer(any())).thenReturn(true);
        when(jwtProvider.validateToken(any())).thenThrow(new TokenAuthenticationException(AuthErrorCode.UNAUTHENTICATED));
    }

    protected void setUpAuthenticated() {
        when(jwtProvider.isStartWithBearer(any())).thenReturn(true);
        when(jwtProvider.validateToken(any())).thenReturn(true);
        when(tokenQueryPort.existsLogoutAccessTokenById(any())).thenReturn(false);
        when(tokenQueryPort.existsLogoutRefreshTokenById(any())).thenReturn(false);
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

    protected void setUpExpired() {
        when(jwtProvider.isStartWithBearer(any())).thenReturn(true);
        when(jwtProvider.validateToken(any())).thenThrow(new TokenException(AuthErrorCode.EXPIRED));
    }
}
