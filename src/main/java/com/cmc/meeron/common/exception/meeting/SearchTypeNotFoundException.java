package com.cmc.meeron.common.exception.meeting;

import com.cmc.meeron.common.exception.ApplicationException;
import com.cmc.meeron.common.exception.ErrorEnumCode;

public class SearchTypeNotFoundException extends ApplicationException {

    private static final ErrorEnumCode CODE = MeetingErrorCode.NOT_ALLOW_SEARCH_TYPE;

    private SearchTypeNotFoundException(ErrorEnumCode errorEnumCode, String message) {
        super(errorEnumCode, message);
    }

    public SearchTypeNotFoundException() {
        this(CODE, CODE.getMessage());
    }
}
