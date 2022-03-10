package com.cmc.meeron.common.security;

import com.cmc.meeron.auth.application.port.out.TokenQueryPort;
import com.cmc.meeron.common.exception.auth.AuthErrorCode;
import com.cmc.meeron.common.exception.auth.TokenAuthenticationException;
import com.cmc.meeron.common.exception.auth.TokenException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class TokenAuthenticationFilter extends OncePerRequestFilter {

    private final CustomUserDetailsService customUserDetailsService;
    private final JwtProvider jwtProvider;
    private final TokenQueryPort tokenQueryPort;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            String jwt = getJwtFromRequest(request);
            if (isValid(jwt)) {
                String email = jwtProvider.getUserEmail(jwt);
                UserDetails userDetails = customUserDetailsService.loadUserByUsername(email);
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (TokenException e) {
            throw e;
        } catch (Exception e) {
            throw new TokenAuthenticationException(AuthErrorCode.UNAUTHENTICATED);
        }
        filterChain.doFilter(request, response);
    }

    private String getJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (StringUtils.hasText(bearerToken) && jwtProvider.isStartWithBearer(bearerToken)) {
            return bearerToken.substring(7);
        }
        return null;
    }

    private boolean isValid(String jwt) {
        return StringUtils.hasText(jwt)
                && jwtProvider.validateToken(jwt)
                && !tokenQueryPort.existsLogoutAccessTokenById(jwt)
                && !tokenQueryPort.existsLogoutRefreshTokenById(jwt);
    }
}
