package com.cmc.meeron.user;

import com.cmc.meeron.user.domain.Role;
import com.cmc.meeron.user.domain.User;

public class UserFixture {

    public static final User USER = User.builder()
            .id(1L)
            .name("고범석")
            .profileImageUrl("")
            .email("kobumssh@naver.com")
            .role(Role.USER)
            .build();

    public static final User NOT_NAMED_USER = User.builder()
            .id(2L)
            .profileImageUrl("")
            .email("test@test.com")
            .role(Role.USER)
            .build();
}
