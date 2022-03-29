package com.cmc.meeron.common.exception.workspace;

import com.cmc.meeron.common.exception.ApplicationException;
import com.cmc.meeron.common.exception.ErrorEnumCode;

public class NotAdminException extends ApplicationException {

    private static final ErrorEnumCode CODE = WorkspaceUserErrorCode.ONLY_ADMIN;

    private NotAdminException(ErrorEnumCode errorEnumCode, String message) {
        super(errorEnumCode, message);
    }

    public NotAdminException() {
        this(CODE, CODE.getMessage());
    }
}
