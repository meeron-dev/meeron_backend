package com.cmc.meeron.common.exception.meeting;

import com.cmc.meeron.common.exception.ApplicationException;
import com.cmc.meeron.common.exception.CommonErrorCode;
import com.cmc.meeron.common.exception.ErrorEnumCode;

public class MeetingNotFoundException extends ApplicationException {

    private static final String MESSAGE = "존재하지 않는 회의입니다.";
    private static final ErrorEnumCode CODE = CommonErrorCode.APPLICATION_EXCEPTION;

    private MeetingNotFoundException(ErrorEnumCode errorEnumCode, String message) {
        super(errorEnumCode, message);
    }

    public MeetingNotFoundException(String message) {
        this(CODE, message);
    }

    public MeetingNotFoundException() {
        this(CODE, MESSAGE);
    }
}
