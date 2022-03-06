package com.cmc.meeron.common.exception.meeting;

import com.cmc.meeron.common.exception.ApplicationException;

public class NotWorkspacesTeamException extends ApplicationException {

    private static final String MESSAGE = "해당 팀은 워크스페이스에 속해 있지 않습니다.";

    public NotWorkspacesTeamException(String message) {
        super(message);
    }

    public NotWorkspacesTeamException() {
        this(MESSAGE);
    }
}
