package com.cmc.meeron.common.exception.meeting;

import com.cmc.meeron.common.exception.ApplicationException;
import com.cmc.meeron.common.exception.CommonErrorCode;
import com.cmc.meeron.common.exception.ErrorEnumCode;

public class AttendeeDuplicateException extends ApplicationException {

    private static final String MESSAGE = "이미 참여중인 유저가 있습니다.";
    private static final ErrorEnumCode CODE = CommonErrorCode.APPLICATION_EXCEPTION;

    private AttendeeDuplicateException(ErrorEnumCode errorEnumCode, String message) {
        super(errorEnumCode, message);
    }

    public AttendeeDuplicateException() {
        this(CODE, MESSAGE);
    }
}
