package com.cmc.meeron.common.exception.file;

import com.cmc.meeron.common.exception.ErrorEnumCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum FileErrorCode implements ErrorEnumCode {

    FILE_UPLOAD(1200, "파일 업로드 중 예외가 발생했습니다."),
    FILE_EXTENSION_NOT_FOUND(1201, "파일 확장자 명을 찾을 수 없습니다."),
    ;

    private final int code;
    private final String message;
}
