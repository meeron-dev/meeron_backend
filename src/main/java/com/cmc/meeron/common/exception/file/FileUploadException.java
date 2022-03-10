package com.cmc.meeron.common.exception.file;

import com.cmc.meeron.common.exception.ApplicationException;
import com.cmc.meeron.common.exception.CommonErrorCode;
import com.cmc.meeron.common.exception.ErrorEnumCode;

public class FileUploadException extends ApplicationException {

    private static final String MESSAGE = "파일 업로드 중 에러가 발생했습니다.";
    private static final ErrorEnumCode CODE = CommonErrorCode.APPLICATION_EXCEPTION;

    private FileUploadException(ErrorEnumCode errorEnumCode, String message) {
        super(errorEnumCode, message);
    }

    public FileUploadException(String message) {
        this(CODE, message);
    }

    public FileUploadException() {
        this(CODE, MESSAGE);
    }
}
