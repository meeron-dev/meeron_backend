package com.cmc.meeron.user.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum UserProvider {

    KAKAO("KAKAO"),
    ;

    private final String provider;
}
