package com.cmc.meeron.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CommonErrorCode implements ErrorEnumCode {

    APPLICATION_EXCEPTION(1000, "비즈니스 로직에 대한 예외입니다."),
    BIND_EXCEPTION(1001, "입력 값에 대한 예외입니다."),
    TYPE_MISMATCH_EXCEPTION(1002, "형변환 중 예외가 발생했습니다. 값을 입력형식에 맞게 입력해주세요.")
    ;

    private final int code;
    private String message;
}
