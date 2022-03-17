package com.cmc.meeron.common.exception.meeting;

import com.cmc.meeron.common.exception.ApplicationException;
import com.cmc.meeron.common.exception.CommonErrorCode;
import com.cmc.meeron.common.exception.ErrorEnumCode;

public class SearchTypeNotFoundException extends ApplicationException {

    private static final String MESSAGE = "검색 유형은 'WORKSPACE', 'WORKSPACE_USER', 'TEAM' 중 하나를 입력해주세요.";
    private static final ErrorEnumCode CODE = CommonErrorCode.APPLICATION_EXCEPTION;

    private SearchTypeNotFoundException(ErrorEnumCode errorEnumCode, String message) {
        super(errorEnumCode, message);
    }

    public SearchTypeNotFoundException() {
        this(CODE, MESSAGE);
    }
}
