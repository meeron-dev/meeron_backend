package com.cmc.meeron.common.exception.meeting;

import com.cmc.meeron.common.exception.ApplicationException;
import com.cmc.meeron.common.exception.ErrorEnumCode;

public class AttendeeDuplicateException extends ApplicationException {

    private static final ErrorEnumCode CODE = MeetingErrorCode.ATTENDEE_DUPLICATE;

    private AttendeeDuplicateException(ErrorEnumCode errorEnumCode, String message) {
        super(errorEnumCode, message);
    }

    public AttendeeDuplicateException() {
        this(CODE, CODE.getMessage());
    }
}
