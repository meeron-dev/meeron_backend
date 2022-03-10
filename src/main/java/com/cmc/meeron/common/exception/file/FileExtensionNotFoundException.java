package com.cmc.meeron.common.exception.file;

import com.cmc.meeron.common.exception.ApplicationException;
import com.cmc.meeron.common.exception.CommonErrorCode;
import com.cmc.meeron.common.exception.ErrorEnumCode;

public class FileExtensionNotFoundException extends ApplicationException {

    private static final String MESSAGE = "파일 확장자 명을 찾을 수 없습니다.";
    private static final ErrorEnumCode CODE = CommonErrorCode.APPLICATION_EXCEPTION;

    private FileExtensionNotFoundException(ErrorEnumCode errorEnumCode, String message) {
        super(errorEnumCode, message);
    }

    public FileExtensionNotFoundException() {
        this(CODE, MESSAGE);
    }
}
