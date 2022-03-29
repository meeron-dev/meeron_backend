package com.cmc.meeron.common.exception.meeting;

import com.cmc.meeron.common.exception.ApplicationException;
import com.cmc.meeron.common.exception.ErrorEnumCode;

public class MeetingNotFoundException extends ApplicationException {

    private static final ErrorEnumCode CODE = MeetingErrorCode.NOT_FOUND_MEETING;

    private MeetingNotFoundException(ErrorEnumCode errorEnumCode, String message) {
        super(errorEnumCode, message);
    }

    public MeetingNotFoundException() {
        this(CODE, CODE.getMessage());
    }
}
