package com.cmc.meeron.support.security;

import com.cmc.meeron.common.security.AuthUser;
import com.cmc.meeron.user.domain.Role;
import com.cmc.meeron.user.domain.User;
import com.cmc.meeron.user.domain.UserProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

import static org.springframework.security.core.authority.AuthorityUtils.createAuthorityList;

final class WithMockJwtSecurityContextFactory implements WithSecurityContextFactory<WithMockJwt> {

    @Override
    public SecurityContext createSecurityContext(WithMockJwt annotation) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();

        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(
                        AuthUser.of(createUser(annotation.id(), annotation.email())),
                        null,
                        createAuthorityList(annotation.role().getRole()));
        context.setAuthentication(authentication);
        return context;
    }

    private User createUser(Long id, String email) {
        return User.builder()
                .id(id)
                .email(email)
                .role(Role.USER)
                .userProvider(UserProvider.KAKAO)
                .build();
    }
}
