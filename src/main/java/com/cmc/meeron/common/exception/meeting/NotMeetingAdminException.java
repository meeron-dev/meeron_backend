package com.cmc.meeron.common.exception.meeting;

import com.cmc.meeron.common.exception.ApplicationException;
import com.cmc.meeron.common.exception.ErrorEnumCode;

public class NotMeetingAdminException extends ApplicationException {

    private static final ErrorEnumCode CODE = MeetingErrorCode.NOT_MEETING_ADMIN;

    private NotMeetingAdminException(ErrorEnumCode errorEnumCode, String message) {
        super(errorEnumCode, message);
    }

    public NotMeetingAdminException() {
        this(CODE, CODE.getMessage());
    }
}
