package com.cmc.meeron.common.exception.file;

import com.cmc.meeron.common.exception.ApplicationException;

public class FileUploadException extends ApplicationException {

    private static final String MESSAGE = "파일 업로드 중 에러가 발생했습니다.";

    public FileUploadException(String message) {
        super(message);
    }

    public FileUploadException() {
        this(MESSAGE);
    }
}
