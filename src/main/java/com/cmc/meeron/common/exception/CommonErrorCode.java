package com.cmc.meeron.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CommonErrorCode implements ErrorEnumCode {

    APPLICATION_EXCEPTION(1000, "비즈니스 로직에 대한 예외입니다."),
    BIND_EXCEPTION(1001, "입력 값에 대한 예외입니다."),
    ;

    private final int code;
    private String message;
}
