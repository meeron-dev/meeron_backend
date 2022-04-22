package com.cmc.meeron.common.exception;

import lombok.Getter;

@Getter
public class ApplicationException extends RuntimeException {

    private ErrorEnumCode errorEnumCode;

    public ApplicationException(ErrorEnumCode errorEnumCode) {
        super(errorEnumCode.getMessage());
        this.errorEnumCode = errorEnumCode;
    }

    public ApplicationException(ErrorEnumCode errorEnumCode, String message) {
        super(message);
        this.errorEnumCode = errorEnumCode;
    }
}
