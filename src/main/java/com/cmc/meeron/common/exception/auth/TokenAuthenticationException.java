package com.cmc.meeron.common.exception.auth;

import com.cmc.meeron.common.exception.ApplicationException;

public class TokenAuthenticationException extends ApplicationException {

    private static final String MESSAGE = "인증에 실패했습니다.";

    public TokenAuthenticationException(String message) {
        super(message);
    }

    public TokenAuthenticationException() {
        this(MESSAGE);
    }
}
