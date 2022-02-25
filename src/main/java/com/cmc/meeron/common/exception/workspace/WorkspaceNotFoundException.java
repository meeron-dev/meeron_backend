package com.cmc.meeron.common.exception.workspace;

import com.cmc.meeron.common.exception.ApplicationException;

public class WorkspaceNotFoundException extends ApplicationException {

    private static final String MESSAGE = "해당 워크스페이스를 찾을 수 없습니다.";

    public WorkspaceNotFoundException(String message) {
        super(message);
    }

    public WorkspaceNotFoundException() {
        this(MESSAGE);
    }
}
