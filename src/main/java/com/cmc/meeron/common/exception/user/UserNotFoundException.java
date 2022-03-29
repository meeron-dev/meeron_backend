package com.cmc.meeron.common.exception.user;

import com.cmc.meeron.common.exception.ApplicationException;
import com.cmc.meeron.common.exception.ErrorEnumCode;

public class UserNotFoundException extends ApplicationException {

    private static final ErrorEnumCode CODE = UserErrorCode.NOT_FOUND_USER;

    private UserNotFoundException(ErrorEnumCode errorEnumCode, String message) {
        super(errorEnumCode, message);
    }

    public UserNotFoundException() {
        this(CODE, CODE.getMessage());
    }
}
