package com.cmc.meeron.common.exception.meeting;

import com.cmc.meeron.common.exception.ApplicationException;
import com.cmc.meeron.common.exception.CommonErrorCode;
import com.cmc.meeron.common.exception.ErrorEnumCode;

public class AgendaNotFoundException extends ApplicationException {

    private static final String MESSAGE = "존재하지 않는 아젠다입니다.";
    private static final ErrorEnumCode CODE = CommonErrorCode.APPLICATION_EXCEPTION;

    private AgendaNotFoundException(ErrorEnumCode errorEnumCode, String message) {
        super(errorEnumCode, message);
    }

    public AgendaNotFoundException(String message) {
        this(CODE, message);
    }

    public AgendaNotFoundException() {
        this(CODE, MESSAGE);
    }
}
