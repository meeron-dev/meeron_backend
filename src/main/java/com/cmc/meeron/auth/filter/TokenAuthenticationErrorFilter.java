package com.cmc.meeron.auth.filter;

import com.cmc.meeron.common.exception.ErrorResponse;
import com.cmc.meeron.common.exception.auth.TokenAuthenticationException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class TokenAuthenticationErrorFilter extends OncePerRequestFilter {

    private final ObjectMapper objectMapper;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            filterChain.doFilter(request, response);
        } catch (TokenAuthenticationException tokenAuthenticationException) {
            log.error("[TokenAuthentication Exception] {}", tokenAuthenticationException.getMessage());
            response.setCharacterEncoding("UTF-8");
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            String json = objectMapper.writeValueAsString(
                    ErrorResponse.fromUnauthorized(tokenAuthenticationException.getMessage()));
            response.getWriter().write(json);
        }
    }
}
