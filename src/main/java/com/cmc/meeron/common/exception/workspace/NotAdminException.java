package com.cmc.meeron.common.exception.workspace;

import com.cmc.meeron.common.exception.ApplicationException;
import com.cmc.meeron.common.exception.CommonErrorCode;
import com.cmc.meeron.common.exception.ErrorEnumCode;

public class NotAdminException extends ApplicationException {

    private static final String MESSAGE = "해당 기능은 관리자만 실행할 수 있습니다.";
    private static final ErrorEnumCode CODE = CommonErrorCode.APPLICATION_EXCEPTION;

    private NotAdminException(ErrorEnumCode errorEnumCode, String message) {
        super(errorEnumCode, message);
    }

    public NotAdminException(String message) {
        super(CODE, message);
    }

    public NotAdminException() {
        this(CODE, MESSAGE);
    }
}
