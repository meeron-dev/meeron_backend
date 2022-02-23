package com.cmc.meeron.common.exception.user;

import com.cmc.meeron.common.exception.ApplicationException;

public class WorkspaceUserNotFoundException extends ApplicationException {

    private static final String MESSAGE = "워크스페이스 유저를 찾지 못했습니다.";

    public WorkspaceUserNotFoundException(String message) {
        super(message);
    }

    public WorkspaceUserNotFoundException() {
        this(MESSAGE);
    }
}
