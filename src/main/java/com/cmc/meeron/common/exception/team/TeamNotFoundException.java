package com.cmc.meeron.common.exception.team;

import com.cmc.meeron.common.exception.ApplicationException;
import com.cmc.meeron.common.exception.CommonErrorCode;
import com.cmc.meeron.common.exception.ErrorEnumCode;

public class TeamNotFoundException extends ApplicationException {

    private static final String MESSAGE = "팀을 찾지 못했습니다.";
    private static final ErrorEnumCode CODE = CommonErrorCode.APPLICATION_EXCEPTION;

    private TeamNotFoundException(ErrorEnumCode errorEnumCode, String message) {
        super(errorEnumCode, message);
    }

    public TeamNotFoundException(String message) {
        this(CODE, message);
    }

    public TeamNotFoundException() {
        this(CODE, MESSAGE);
    }
}
