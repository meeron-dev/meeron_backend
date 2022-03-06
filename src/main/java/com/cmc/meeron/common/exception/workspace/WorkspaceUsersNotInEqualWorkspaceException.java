package com.cmc.meeron.common.exception.workspace;

import com.cmc.meeron.common.exception.ApplicationException;

public class WorkspaceUsersNotInEqualWorkspaceException extends ApplicationException {

    private static final String MESSAGE = "해당 유저는 같은 워크스페이스에 속해있지 않습니다.";

    public WorkspaceUsersNotInEqualWorkspaceException(String message) {
        super(message);
    }

    public WorkspaceUsersNotInEqualWorkspaceException() {
        this(MESSAGE);
    }
}
