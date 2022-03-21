package com.cmc.meeron.common.exception.user;

import com.cmc.meeron.common.exception.ApplicationException;
import com.cmc.meeron.common.exception.CommonErrorCode;
import com.cmc.meeron.common.exception.ErrorEnumCode;

public class NicknameDuplicateException extends ApplicationException {

    private static final String MESSAGE = "워크스페이스 내 닉네임은 중복될 수 없습니다.";
    private static final ErrorEnumCode CODE = CommonErrorCode.APPLICATION_EXCEPTION;

    private NicknameDuplicateException(ErrorEnumCode errorEnumCode, String message) {
        super(errorEnumCode, message);
    }

    public NicknameDuplicateException(String message) {
        super(CODE, message);
    }

    public NicknameDuplicateException() {
        this(CODE, MESSAGE);
    }
}
