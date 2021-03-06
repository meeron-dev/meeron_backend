package com.cmc.meeron.common.exception.workspace;

import com.cmc.meeron.common.exception.ApplicationException;
import com.cmc.meeron.common.exception.ErrorEnumCode;

public class WorkspaceUserNotFoundException extends ApplicationException {

    private static final ErrorEnumCode CODE = WorkspaceUserErrorCode.NOT_FOUND;

    private WorkspaceUserNotFoundException(ErrorEnumCode errorEnumCode, String message) {
        super(errorEnumCode, message);
    }

    public WorkspaceUserNotFoundException(String message) {
        super(CODE, message);
    }

    public WorkspaceUserNotFoundException() {
        this(CODE, CODE.getMessage());
    }
}
