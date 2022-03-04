package com.cmc.meeron.common.security;

import com.cmc.meeron.user.application.port.out.UserQueryPort;
import com.cmc.meeron.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Component
@Transactional(readOnly = true)
public class CustomUserDetailsService implements UserDetailsService {

    private final String MESSAGE = "존재하지 않는 회원입니다.";
    private final UserQueryPort userQueryPort;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userQueryPort.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException(MESSAGE));
        return AuthUser.of(user);
    }
}
