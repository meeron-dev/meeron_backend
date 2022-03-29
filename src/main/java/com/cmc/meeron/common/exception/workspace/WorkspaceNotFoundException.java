package com.cmc.meeron.common.exception.workspace;

import com.cmc.meeron.common.exception.ApplicationException;
import com.cmc.meeron.common.exception.ErrorEnumCode;

public class WorkspaceNotFoundException extends ApplicationException {

    private static final ErrorEnumCode CODE = WorkspaceErrorCode.NOT_FOUND;

    private WorkspaceNotFoundException(ErrorEnumCode errorEnumCode, String message) {
        super(errorEnumCode, message);
    }

    public WorkspaceNotFoundException() {
        this(CODE, CODE.getMessage());
    }
}
