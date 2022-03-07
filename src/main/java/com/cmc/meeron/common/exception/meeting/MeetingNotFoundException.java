package com.cmc.meeron.common.exception.meeting;

import com.cmc.meeron.common.exception.ApplicationException;

public class MeetingNotFoundException extends ApplicationException {

    private static final String MESSAGE = "존재하지 않는 회의입니다.";

    public MeetingNotFoundException(String message) {
        super(message);
    }

    public MeetingNotFoundException() {
        this(MESSAGE);
    }
}
