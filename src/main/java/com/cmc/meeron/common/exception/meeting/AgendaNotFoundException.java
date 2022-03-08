package com.cmc.meeron.common.exception.meeting;

import com.cmc.meeron.common.exception.ApplicationException;

public class AgendaNotFoundException extends ApplicationException {

    private static final String MESSAGE = "존재하지 않는 아젠다입니다.";

    public AgendaNotFoundException(String message) {
        super(message);
    }

    public AgendaNotFoundException() {
        this(MESSAGE);
    }
}
