package com.cmc.meeron.common.exception.auth;

import com.cmc.meeron.common.exception.ApplicationException;

public class RefreshTokenNotExistException extends ApplicationException {

    private static final String MESSAGE = "Refresh Token이 저장되어있지 않습니다.";

    public RefreshTokenNotExistException(String message) {
        super(message);
    }

    public RefreshTokenNotExistException() {
        this(MESSAGE);
    }
}
