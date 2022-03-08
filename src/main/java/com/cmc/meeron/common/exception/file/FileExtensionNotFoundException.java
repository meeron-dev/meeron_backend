package com.cmc.meeron.common.exception.file;

import com.cmc.meeron.common.exception.ApplicationException;

public class FileExtensionNotFoundException extends ApplicationException {

    private static final String MESSAGE = "파일 확장자 명을 찾을 수 없습니다.";

    public FileExtensionNotFoundException(String message) {
        super(message);
    }

    public FileExtensionNotFoundException() {
        this(MESSAGE);
    }
}
