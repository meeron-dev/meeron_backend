package com.cmc.meeron.common.exception.meeting;

import com.cmc.meeron.common.exception.ApplicationException;
import com.cmc.meeron.common.exception.ErrorEnumCode;

public class AttendeeNotFoundException extends ApplicationException {

    private static final ErrorEnumCode CODE = MeetingErrorCode.NOT_FOUND_ATTENDEE;

    private AttendeeNotFoundException(ErrorEnumCode errorEnumCode, String message) {
        super(errorEnumCode, message);
    }

    public AttendeeNotFoundException() {
        this(CODE, CODE.getMessage());
    }
}
