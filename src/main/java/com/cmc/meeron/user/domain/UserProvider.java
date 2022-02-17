package com.cmc.meeron.user.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum UserProvider {

    KAKAO("KAKAO"),
    APPLE("APPLE")
    ;

    private final String provider;
}
