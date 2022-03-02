package com.cmc.meeron.auth.handler;

import com.cmc.meeron.auth.domain.AuthUser;
import com.cmc.meeron.user.domain.User;
import com.cmc.meeron.user.domain.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Component
@Transactional
public class CustomUserDetailsService implements UserDetailsService {

    private final String MESSAGE = "존재하지 않는 회원입니다.";
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException(MESSAGE));
        return AuthUser.of(user);
    }
}
