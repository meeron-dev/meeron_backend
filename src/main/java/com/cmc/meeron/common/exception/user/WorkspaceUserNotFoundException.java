package com.cmc.meeron.common.exception.user;

import com.cmc.meeron.common.exception.ApplicationException;
import com.cmc.meeron.common.exception.CommonErrorCode;
import com.cmc.meeron.common.exception.ErrorEnumCode;

public class WorkspaceUserNotFoundException extends ApplicationException {

    private static final String MESSAGE = "워크스페이스 유저를 찾지 못했습니다.";
    private static final ErrorEnumCode CODE = CommonErrorCode.APPLICATION_EXCEPTION;

    private WorkspaceUserNotFoundException(ErrorEnumCode errorEnumCode, String message) {
        super(errorEnumCode, message);
    }

    public WorkspaceUserNotFoundException(String message) {
        super(CODE, message);
    }

    public WorkspaceUserNotFoundException() {
        this(CODE, MESSAGE);
    }
}
