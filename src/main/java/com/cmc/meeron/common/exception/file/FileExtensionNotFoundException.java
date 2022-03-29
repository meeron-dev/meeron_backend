package com.cmc.meeron.common.exception.file;

import com.cmc.meeron.common.exception.ApplicationException;
import com.cmc.meeron.common.exception.ErrorEnumCode;

public class FileExtensionNotFoundException extends ApplicationException {

    private static final ErrorEnumCode CODE = FileErrorCode.FILE_EXTENSION_NOT_FOUND;

    private FileExtensionNotFoundException(ErrorEnumCode errorEnumCode, String message) {
        super(errorEnumCode, message);
    }

    public FileExtensionNotFoundException() {
        this(CODE, CODE.getMessage());
    }
}
