package com.cmc.meeron.common.exception.team;

import com.cmc.meeron.common.exception.ApplicationException;
import com.cmc.meeron.common.exception.CommonErrorCode;
import com.cmc.meeron.common.exception.ErrorEnumCode;

public class PreviousBelongToTeamException extends ApplicationException {

    private static final String MESSAGE = "이미 팀이 존재하는 유저가 있습니다.";
    private static final ErrorEnumCode CODE = CommonErrorCode.APPLICATION_EXCEPTION;

    private PreviousBelongToTeamException(ErrorEnumCode errorEnumCode, String message) {
        super(errorEnumCode, message);
    }

    public PreviousBelongToTeamException(String message) {
        this(CODE, message);
    }

    public PreviousBelongToTeamException() {
        this(CODE, MESSAGE);
    }
}
