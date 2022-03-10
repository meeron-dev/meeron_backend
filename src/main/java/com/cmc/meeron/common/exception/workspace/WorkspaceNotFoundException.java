package com.cmc.meeron.common.exception.workspace;

import com.cmc.meeron.common.exception.ApplicationException;
import com.cmc.meeron.common.exception.CommonErrorCode;
import com.cmc.meeron.common.exception.ErrorEnumCode;

public class WorkspaceNotFoundException extends ApplicationException {

    private static final String MESSAGE = "해당 워크스페이스를 찾을 수 없습니다.";
    private static final ErrorEnumCode CODE = CommonErrorCode.APPLICATION_EXCEPTION;

    private WorkspaceNotFoundException(ErrorEnumCode errorEnumCode, String message) {
        super(errorEnumCode, message);
    }

    public WorkspaceNotFoundException(String message) {
        super(CODE, message);
    }

    public WorkspaceNotFoundException() {
        this(CODE, MESSAGE);
    }
}
