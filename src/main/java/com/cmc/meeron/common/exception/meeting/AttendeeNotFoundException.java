package com.cmc.meeron.common.exception.meeting;

import com.cmc.meeron.common.exception.ApplicationException;
import com.cmc.meeron.common.exception.CommonErrorCode;
import com.cmc.meeron.common.exception.ErrorEnumCode;

public class AttendeeNotFoundException extends ApplicationException {

    private static final String MESSAGE = "존재하지 않는 참가자입니다.";
    private static final ErrorEnumCode CODE = CommonErrorCode.APPLICATION_EXCEPTION;

    private AttendeeNotFoundException(ErrorEnumCode errorEnumCode, String message) {
        super(errorEnumCode, message);
    }

    public AttendeeNotFoundException() {
        this(CODE, MESSAGE);
    }
}
