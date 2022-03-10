package com.cmc.meeron.common.exception.meeting;

import com.cmc.meeron.common.exception.ApplicationException;
import com.cmc.meeron.common.exception.CommonErrorCode;
import com.cmc.meeron.common.exception.ErrorEnumCode;

public class MeetingSearchTypeNotFoundException extends ApplicationException {

    private static final String MESSAGE = "해당 회의 조회 타입은 존재하지 않습니다.";
    private static final ErrorEnumCode CODE = CommonErrorCode.APPLICATION_EXCEPTION;

    private MeetingSearchTypeNotFoundException(ErrorEnumCode errorEnumCode, String message) {
        super(errorEnumCode, message);
    }

    public MeetingSearchTypeNotFoundException(String message) {
        super(CODE, message);
    }

    public MeetingSearchTypeNotFoundException() {
        this(CODE, MESSAGE);
    }
}
