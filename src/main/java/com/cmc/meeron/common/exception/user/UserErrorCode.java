package com.cmc.meeron.common.exception.user;

import com.cmc.meeron.common.exception.ErrorEnumCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum UserErrorCode implements ErrorEnumCode {

    NOT_FOUND_USER(1500, "유저를 찾지 못했습니다."),
    ;

    private final int code;
    private final String message;
}
