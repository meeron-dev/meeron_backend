package com.cmc.meeron.common.exception.team;

import com.cmc.meeron.common.exception.ApplicationException;
import com.cmc.meeron.common.exception.ErrorEnumCode;

public class TeamNotFoundException extends ApplicationException {

    private static final ErrorEnumCode CODE = TeamErrorCode.NOT_FOUND_TEAM;

    private TeamNotFoundException(ErrorEnumCode errorEnumCode, String message) {
        super(errorEnumCode, message);
    }

    public TeamNotFoundException() {
        this(CODE, CODE.getMessage());
    }
}
