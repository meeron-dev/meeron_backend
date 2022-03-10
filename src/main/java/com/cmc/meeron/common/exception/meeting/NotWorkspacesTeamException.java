package com.cmc.meeron.common.exception.meeting;

import com.cmc.meeron.common.exception.ApplicationException;
import com.cmc.meeron.common.exception.CommonErrorCode;
import com.cmc.meeron.common.exception.ErrorEnumCode;

public class NotWorkspacesTeamException extends ApplicationException {

    private static final String MESSAGE = "해당 팀은 워크스페이스에 속해 있지 않습니다.";
    private static final ErrorEnumCode CODE = CommonErrorCode.APPLICATION_EXCEPTION;

    private NotWorkspacesTeamException(ErrorEnumCode errorEnumCode, String message) {
        super(errorEnumCode, message);
    }

    public NotWorkspacesTeamException() {
        this(CODE, MESSAGE);
    }
}
