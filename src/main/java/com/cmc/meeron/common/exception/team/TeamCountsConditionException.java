package com.cmc.meeron.common.exception.team;

import com.cmc.meeron.common.exception.ApplicationException;
import com.cmc.meeron.common.exception.ErrorEnumCode;

public class TeamCountsConditionException extends ApplicationException {

    private static final ErrorEnumCode CODE = TeamErrorCode.WORKSPACE_IN_TEAM_COUNT_OVER;

    private TeamCountsConditionException(ErrorEnumCode errorEnumCode, String message) {
        super(errorEnumCode, message);
    }

    public TeamCountsConditionException() {
        this(CODE, CODE.getMessage());
    }
}
