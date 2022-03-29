package com.cmc.meeron.common.exception.workspace;

import com.cmc.meeron.common.exception.ApplicationException;
import com.cmc.meeron.common.exception.ErrorEnumCode;

public class NotAllFoundWorkspaceUsersException extends ApplicationException {

    private static final ErrorEnumCode CODE = WorkspaceUserErrorCode.NOT_ALL_FOUND;

    private NotAllFoundWorkspaceUsersException(ErrorEnumCode errorEnumCode, String message) {
        super(errorEnumCode, message);
    }

    public NotAllFoundWorkspaceUsersException() {
        this(CODE, CODE.getMessage());
    }
}
