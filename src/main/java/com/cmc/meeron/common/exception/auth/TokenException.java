package com.cmc.meeron.common.exception.auth;

import com.cmc.meeron.common.exception.ApplicationException;
import com.cmc.meeron.common.exception.ErrorEnumCode;

public class TokenException extends ApplicationException {

    private static final String MESSAGE = "만료된 토큰입니다.";
    private ErrorEnumCode errorEnumCode;

    private TokenException(ErrorEnumCode errorEnumCode, String message) {
        super(errorEnumCode, message);
    }

    public TokenException(ErrorEnumCode errorEnumCode) {
        this(errorEnumCode, MESSAGE);
    }

    public TokenException() {
        this(AuthErrorCode.UNAUTHENTICATED, MESSAGE);
    }
}
