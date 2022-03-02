package com.cmc.meeron.common.exception.meeting;

import com.cmc.meeron.common.exception.ApplicationException;

public class MeetingSearchTypeNotFoundException extends ApplicationException {

    private static final String MESSAGE = "해당 회의 조회 타입은 존재하지 않습니다.";

    public MeetingSearchTypeNotFoundException(String message) {
        super(message);
    }

    public MeetingSearchTypeNotFoundException() {
        this(MESSAGE);
    }
}
