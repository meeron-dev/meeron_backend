package com.cmc.meeron.common.exception.team;

import com.cmc.meeron.common.exception.ApplicationException;
import com.cmc.meeron.common.exception.CommonErrorCode;
import com.cmc.meeron.common.exception.ErrorEnumCode;

public class TeamCountsConditionException extends ApplicationException {

    private static final String MESSAGE = "워크스페이스 내에서는 다섯 팀까지 보유 가능합니다.";
    private static final ErrorEnumCode CODE = CommonErrorCode.APPLICATION_EXCEPTION;

    private TeamCountsConditionException(ErrorEnumCode errorEnumCode, String message) {
        super(errorEnumCode, message);
    }

    public TeamCountsConditionException(String message) {
        this(CODE, message);
    }

    public TeamCountsConditionException() {
        this(CODE, MESSAGE);
    }
}
