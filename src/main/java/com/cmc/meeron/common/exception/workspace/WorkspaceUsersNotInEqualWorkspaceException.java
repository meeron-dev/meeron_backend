package com.cmc.meeron.common.exception.workspace;

import com.cmc.meeron.common.exception.ApplicationException;
import com.cmc.meeron.common.exception.CommonErrorCode;
import com.cmc.meeron.common.exception.ErrorEnumCode;

public class WorkspaceUsersNotInEqualWorkspaceException extends ApplicationException {

    private static final String MESSAGE = "해당 유저는 같은 워크스페이스에 속해있지 않습니다.";
    private static final ErrorEnumCode CODE = CommonErrorCode.APPLICATION_EXCEPTION;

    private WorkspaceUsersNotInEqualWorkspaceException(ErrorEnumCode errorEnumCode, String message) {
        super(errorEnumCode, message);
    }

    public WorkspaceUsersNotInEqualWorkspaceException(String message) {
        super(CODE, message);
    }

    public WorkspaceUsersNotInEqualWorkspaceException() {
        this(CODE, MESSAGE);
    }
}
