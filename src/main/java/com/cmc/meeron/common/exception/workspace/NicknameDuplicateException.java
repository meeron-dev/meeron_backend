package com.cmc.meeron.common.exception.workspace;

import com.cmc.meeron.common.exception.ApplicationException;
import com.cmc.meeron.common.exception.ErrorEnumCode;

public class NicknameDuplicateException extends ApplicationException {

    private static final ErrorEnumCode CODE = WorkspaceUserErrorCode.DUPLICATE_NICKNAME;

    private NicknameDuplicateException(ErrorEnumCode errorEnumCode, String message) {
        super(errorEnumCode, message);
    }

    public NicknameDuplicateException() {
        this(CODE, CODE.getMessage());
    }
}
