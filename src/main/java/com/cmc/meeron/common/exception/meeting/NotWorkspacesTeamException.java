package com.cmc.meeron.common.exception.meeting;

import com.cmc.meeron.common.exception.ApplicationException;
import com.cmc.meeron.common.exception.ErrorEnumCode;

public class NotWorkspacesTeamException extends ApplicationException {

    private static final ErrorEnumCode CODE = MeetingErrorCode.OPERATION_TEAM_IS_NOT_IN_WORKSPACE;

    private NotWorkspacesTeamException(ErrorEnumCode errorEnumCode, String message) {
        super(errorEnumCode, message);
    }

    public NotWorkspacesTeamException() {
        this(CODE, CODE.getMessage());
    }
}
