package com.cmc.meeron.common.exception.topic.agenda;

import com.cmc.meeron.common.exception.ErrorEnumCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AgendaErrorCode implements ErrorEnumCode {

    NOT_FOUND(1800, "아젠다를 찾을 수 없습니다."),
    ;

    private final int code;
    private final String message;
}
