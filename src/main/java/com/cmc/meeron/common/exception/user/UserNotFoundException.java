package com.cmc.meeron.common.exception.user;

import com.cmc.meeron.common.exception.ApplicationException;
import com.cmc.meeron.common.exception.CommonErrorCode;
import com.cmc.meeron.common.exception.ErrorEnumCode;

public class UserNotFoundException extends ApplicationException {

    private static final String MESSAGE = "유저를 찾지 못했습니다.";
    private static final ErrorEnumCode CODE = CommonErrorCode.APPLICATION_EXCEPTION;

    private UserNotFoundException(ErrorEnumCode errorEnumCode, String message) {
        super(errorEnumCode, message);
    }

    public UserNotFoundException(String message) {
        super(CODE, message);
    }

    public UserNotFoundException() {
        this(CODE, MESSAGE);
    }
}
