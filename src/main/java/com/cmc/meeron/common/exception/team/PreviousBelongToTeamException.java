package com.cmc.meeron.common.exception.team;

import com.cmc.meeron.common.exception.ApplicationException;
import com.cmc.meeron.common.exception.ErrorEnumCode;

public class PreviousBelongToTeamException extends ApplicationException {

    private static final ErrorEnumCode CODE = TeamErrorCode.PREVIOUS_BELONG_IN_TEAM;

    private PreviousBelongToTeamException(ErrorEnumCode errorEnumCode, String message) {
        super(errorEnumCode, message);
    }

    public PreviousBelongToTeamException() {
        this(CODE, CODE.getMessage());
    }
}
