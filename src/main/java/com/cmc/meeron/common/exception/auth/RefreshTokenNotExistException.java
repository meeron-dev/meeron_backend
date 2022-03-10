package com.cmc.meeron.common.exception.auth;

import com.cmc.meeron.common.exception.ApplicationException;
import com.cmc.meeron.common.exception.ErrorEnumCode;

public class RefreshTokenNotExistException extends ApplicationException {

    private static final String MESSAGE = "Refresh Token이 저장되어있지 않습니다.";
    private static final AuthErrorCode AUTH_ERROR_CODE = AuthErrorCode.NOT_FOUND_REFRESH_TOKEN;

    private RefreshTokenNotExistException(ErrorEnumCode errorEnumCode, String message) {
        super(errorEnumCode, message);
    }

    public RefreshTokenNotExistException(String message) {
        super(AUTH_ERROR_CODE, message);
    }

    public RefreshTokenNotExistException() {
        this(MESSAGE);
    }
}
