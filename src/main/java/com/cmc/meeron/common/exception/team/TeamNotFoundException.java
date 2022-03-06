package com.cmc.meeron.common.exception.team;

import com.cmc.meeron.common.exception.ApplicationException;

public class TeamNotFoundException extends ApplicationException {

    private static final String MESSAGE = "팀을 찾지 못했습니다.";

    public TeamNotFoundException(String message) {
        super(message);
    }

    public TeamNotFoundException() {
        this(MESSAGE);
    }
}
