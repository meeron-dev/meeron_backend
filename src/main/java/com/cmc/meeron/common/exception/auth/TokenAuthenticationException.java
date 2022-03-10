package com.cmc.meeron.common.exception.auth;

import com.cmc.meeron.common.exception.ApplicationException;
import com.cmc.meeron.common.exception.ErrorEnumCode;

public class TokenAuthenticationException extends ApplicationException {

    private static final String MESSAGE = "인증에 실패했습니다.";
    private static final AuthErrorCode AUTH_ERROR_CODE = AuthErrorCode.UNAUTHENTICATED;

    private TokenAuthenticationException(ErrorEnumCode errorEnumCode, String message) {
        super(errorEnumCode, message);
    }

    public TokenAuthenticationException() {
        this(AUTH_ERROR_CODE, MESSAGE);
    }

    public TokenAuthenticationException(ErrorEnumCode errorEnumCode) {
        this(errorEnumCode, MESSAGE);
    }
}
