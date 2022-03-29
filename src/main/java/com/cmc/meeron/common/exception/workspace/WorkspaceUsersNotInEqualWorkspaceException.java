package com.cmc.meeron.common.exception.workspace;

import com.cmc.meeron.common.exception.ApplicationException;
import com.cmc.meeron.common.exception.ErrorEnumCode;

public class WorkspaceUsersNotInEqualWorkspaceException extends ApplicationException {

    private static final ErrorEnumCode CODE = WorkspaceUserErrorCode.NOT_BELONG_TO_WORKSPACE;

    private WorkspaceUsersNotInEqualWorkspaceException(ErrorEnumCode errorEnumCode, String message) {
        super(errorEnumCode, message);
    }

    public WorkspaceUsersNotInEqualWorkspaceException() {
        this(CODE, CODE.getMessage());
    }
}
