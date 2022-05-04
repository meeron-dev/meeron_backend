package com.cmc.meeron.common.exception.topic.agenda;

import com.cmc.meeron.common.exception.ApplicationException;
import com.cmc.meeron.common.exception.ErrorEnumCode;

public class AgendaNotFoundException extends ApplicationException {

    private static final ErrorEnumCode CODE = AgendaErrorCode.NOT_FOUND;

    private AgendaNotFoundException(ErrorEnumCode errorEnumCode, String message) {
        super(errorEnumCode, message);
    }

    public AgendaNotFoundException() {
        this(CODE, CODE.getMessage());
    }
}
