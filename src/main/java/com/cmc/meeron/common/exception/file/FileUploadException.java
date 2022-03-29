package com.cmc.meeron.common.exception.file;

import com.cmc.meeron.common.exception.ApplicationException;
import com.cmc.meeron.common.exception.ErrorEnumCode;

public class FileUploadException extends ApplicationException {

    private static final ErrorEnumCode CODE = FileErrorCode.FILE_UPLOAD;

    private FileUploadException(ErrorEnumCode errorEnumCode, String message) {
        super(errorEnumCode, message);
    }

    public FileUploadException(String message) {
        this(CODE, message);
    }

    public FileUploadException() {
        this(CODE, CODE.getMessage());
    }
}
