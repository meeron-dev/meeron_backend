package com.cmc.meeron.common.exception.meeting;

import com.cmc.meeron.common.exception.ApplicationException;
import com.cmc.meeron.common.exception.ErrorEnumCode;

public class AgendaNotFoundException extends ApplicationException {

    private static final ErrorEnumCode CODE = MeetingErrorCode.NOT_FOUND_AGENDA;

    private AgendaNotFoundException(ErrorEnumCode errorEnumCode, String message) {
        super(errorEnumCode, message);
    }

    public AgendaNotFoundException() {
        this(CODE, CODE.getMessage());
    }
}
